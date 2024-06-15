package com.swarnim.userservice.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserServiceConfigs {
    // Here we are creating the bean(object) of BCryptPasswordEncoder as we have only added the depencency,so whenerver
    // the application starts the spring will find the configuration is present and will see @Bean is there,
    // it will create the bean(object) of BCryptPasswordEncoder.
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
