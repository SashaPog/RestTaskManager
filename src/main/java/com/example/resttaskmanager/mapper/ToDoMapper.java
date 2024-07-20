package com.example.resttaskmanager.mapper;

import com.example.resttaskmanager.dto.ToDoRequest;
import com.example.resttaskmanager.model.ToDo;
import org.mapstruct.Mapper;

@Mapper
public interface ToDoMapper {

    ToDo dtoToModel(ToDoRequest toDoRequest);
}
