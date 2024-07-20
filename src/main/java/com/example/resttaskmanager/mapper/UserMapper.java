package com.example.resttaskmanager.mapper;

import com.example.resttaskmanager.dto.UserRequest;
import com.example.resttaskmanager.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User dtoToModel(UserRequest request);
}
