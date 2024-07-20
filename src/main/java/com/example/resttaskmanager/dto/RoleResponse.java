package com.example.resttaskmanager.dto;

import com.example.resttaskmanager.model.Role;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoleResponse {

    Long id;
    String name;

    public RoleResponse(Role role) {
        id = role.getId();
        name = role.getName();
    }
}
