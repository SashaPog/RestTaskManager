package com.example.resttaskmanager.dto;

import com.example.resttaskmanager.model.Task;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponse {
    Long id;
    String name;
    String priority;
    Long todo_id;
    String state;

    public TaskResponse(Task task){
        id = task.getId();
        name = task.getName();
        priority = task.getPriority().name();
        todo_id = task.getTodo().getId();
        state = task.getState().getName();
    }
}
