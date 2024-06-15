package com.swarnim.userservice.controllers;

import com.swarnim.userservice.dtos.LoginRequestDto;
import com.swarnim.userservice.dtos.LogoutRequestDto;
import com.swarnim.userservice.dtos.SignUpRequestDto;
import com.swarnim.userservice.dtos.UserDto;
import com.swarnim.userservice.models.Token;
import com.swarnim.userservice.models.User;
import com.swarnim.userservice.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
                );
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token logIn(@RequestBody LoginRequestDto requestDto) {
       Token token = userService.login(
               requestDto.getEmail(),
               requestDto.getPassword()
       );
       return token;
    }

    @PostMapping("/logout")
    public ResponseEntity<Token> logOut(@RequestBody LogoutRequestDto requestDto) {
        userService.logout(requestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        User user = userService.validateToken(token);
        return UserDto.from(user);
    }

    @GetMapping("/user/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return null;
    }
}
