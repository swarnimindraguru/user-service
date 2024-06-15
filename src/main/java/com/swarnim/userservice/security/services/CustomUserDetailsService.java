package com.swarnim.userservice.security.services;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swarnim.userservice.models.User;
import com.swarnim.userservice.repositories.UserRepository;
import com.swarnim.userservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@JsonDeserialize

public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User with email "+ username + " doesen't exist.");
        }
        return new CustomUserDetails(optionalUser.get());
    }
}
