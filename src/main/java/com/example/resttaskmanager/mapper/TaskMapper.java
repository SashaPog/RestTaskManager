package com.example.resttaskmanager.mapper;

import com.example.resttaskmanager.dto.TaskRequest;
import com.example.resttaskmanager.model.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    Task dtoToModel(TaskRequest request);
}
