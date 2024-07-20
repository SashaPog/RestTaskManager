package com.example.resttaskmanager.mapper;

import com.example.resttaskmanager.dto.StateRequest;
import com.example.resttaskmanager.model.State;
import org.mapstruct.Mapper;

@Mapper
public interface StateMapper {

    State dtoToModel(StateRequest request);
}
