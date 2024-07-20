package com.example.resttaskmanager.mapper;

import com.example.resttaskmanager.dto.RoleRequest;
import com.example.resttaskmanager.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {

    Role dtoToModel(RoleRequest request);
}
