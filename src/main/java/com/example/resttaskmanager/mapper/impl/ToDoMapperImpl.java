package com.example.resttaskmanager.mapper.impl;

import com.example.resttaskmanager.dto.ToDoRequest;
import com.example.resttaskmanager.mapper.ToDoMapper;
import com.example.resttaskmanager.model.ToDo;
import org.springframework.stereotype.Component;

@Component
public class ToDoMapperImpl implements ToDoMapper {

    @Override
    public ToDo dtoToModel(ToDoRequest toDoRequest) {
        ToDo toDo = new ToDo();

        toDo.setTitle(toDoRequest.getTitle());
        toDo.setCreatedAt(toDoRequest.getCreatedAt());

        return toDo;
    }
}
