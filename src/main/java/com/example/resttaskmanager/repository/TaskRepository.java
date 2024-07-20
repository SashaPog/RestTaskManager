package com.example.resttaskmanager.repository;

import com.example.resttaskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("from Task where todo.id = :todoId")
    List<Task> getByTodoId(long todoId);
}
