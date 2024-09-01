package com.swarnim.userservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swarnim.userservice.dtos.SendEmailMessageDto;
import com.swarnim.userservice.exceptions.InvalidTokenException;
import com.swarnim.userservice.exceptions.UserAlreadyExistException;
import com.swarnim.userservice.exceptions.UserNotFoundException;
import com.swarnim.userservice.models.Token;
import com.swarnim.userservice.models.User;
import com.swarnim.userservice.repositories.TokenRepository;
import com.swarnim.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.common.network.Send;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service

public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository,TokenRepository tokenRepository, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper ){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User Already Exist with this email");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVarified(true);

        //save the user obj to the db
        User savedUser =  userRepository.save(user);

        SendEmailMessageDto messageDto = new SendEmailMessageDto();
        messageDto.setTo(savedUser.getEmail());
        messageDto.setSubject("!! Welcome User !!");
        messageDto.setBody("Thank you for Signing Up. Hope you will have a great experience here.");
        try{
            kafkaTemplate.send(
                    "sendEmail",
                    objectMapper.writeValueAsString(messageDto)
            );
        } catch (Exception e){
            System.out.println("Got some exceptions");
        }
                return savedUser;
    };

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("User with email " + email + " doesn't exist");
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getHashedPassword())){
            throw new UserNotFoundException("Wrong password");
        }
        //Login successful, generate a Token.
        Token token = generateToken(user);
        Token savedToken = tokenRepository.save(token);

        return savedToken;
    }

    private Token generateToken(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        //128 character alphanumeric string.
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);

        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid Token");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());

        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid Token");
        }

        return optionalToken.get().getUser();
    }
}
