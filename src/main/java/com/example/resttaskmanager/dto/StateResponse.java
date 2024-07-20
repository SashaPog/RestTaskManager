package com.example.resttaskmanager.dto;

import com.example.resttaskmanager.model.State;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StateResponse {

    Long id;
    String name;

    public StateResponse(State state) {
        id = state.getId();
        name = state.getName();
    }
}
