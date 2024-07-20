package com.example.resttaskmanager.dto;

import com.example.resttaskmanager.model.ToDo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ToDoResponse {

    long id;

    String title;

    LocalDateTime createdAt;


    long ownerId;


    public ToDoResponse(ToDo toDo) {

        this.id = toDo.getId();
        this.title = toDo.getTitle();
        this.createdAt = toDo.getCreatedAt();
        this.ownerId = toDo.getOwner().getId();
    }
}
