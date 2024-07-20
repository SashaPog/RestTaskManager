package com.example.resttaskmanager.mapper.impl;

import com.example.resttaskmanager.dto.StateRequest;
import com.example.resttaskmanager.mapper.StateMapper;
import com.example.resttaskmanager.model.State;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class StateMapperImpl implements StateMapper {


    @Override
    public State dtoToModel(StateRequest request) {
        State state = new State();
        state.setName(request.getName());
        return state;
    }

}

