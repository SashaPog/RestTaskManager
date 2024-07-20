package com.example.resttaskmanager.mapper.impl;

import com.example.resttaskmanager.dto.UserRequest;
import com.example.resttaskmanager.mapper.UserMapper;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapperImpl implements UserMapper {

    private final RoleService roleService;

    @Override
    public User dtoToModel(UserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        if(request.getRoleName() == null) {
            user.setRole(roleService.readByName("USER"));
        } else {
            user.setRole(roleService.readByName(request.getRoleName()));
        }
        return user;
    }
}
