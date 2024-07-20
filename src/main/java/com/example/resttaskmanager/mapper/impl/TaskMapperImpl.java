package com.example.resttaskmanager.mapper.impl;

import com.example.resttaskmanager.dto.TaskRequest;
import com.example.resttaskmanager.mapper.TaskMapper;
import com.example.resttaskmanager.model.Task;
import com.example.resttaskmanager.service.StateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TaskMapperImpl implements TaskMapper {

    private final StateService stateService;

    @Override
    public Task dtoToModel(TaskRequest request) {
        Task task = new Task();
        task.setName(request.getName());
        task.setPriority(request.getPriority());
        if (request.getStateName() == null){
            task.setState(stateService.getByName("NEW"));
        } else {
            task.setState(stateService.getByName(request.getStateName()));
        }
        return task;
    }
}
