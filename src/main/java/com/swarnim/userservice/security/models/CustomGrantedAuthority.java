package com.swarnim.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.swarnim.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
   private String authority;

    public CustomGrantedAuthority() {

    }

    public CustomGrantedAuthority(Role role){
        this.authority = role.getValue();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
