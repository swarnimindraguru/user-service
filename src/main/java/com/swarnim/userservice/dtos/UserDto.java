package com.swarnim.userservice.dtos;

import com.swarnim.userservice.models.Role;
import com.swarnim.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;

    //to convert User object to userDto and can directly call in controller, we made it status so that we don't need to create obj
    public static UserDto from(User user){
        if(user == null) return null;
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        return dto;
    }
}
