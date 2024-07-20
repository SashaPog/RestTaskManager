package com.example.resttaskmanager.mapper.impl;

import com.example.resttaskmanager.dto.RoleRequest;
import com.example.resttaskmanager.mapper.RoleMapper;
import com.example.resttaskmanager.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role dtoToModel(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        return role;
    }
}
