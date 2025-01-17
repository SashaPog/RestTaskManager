package com.example.resttaskmanager.repository;

import com.example.resttaskmanager.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query(value = "select id, title, created_at, owner_id from todos where owner_id = ?1 union " +
            "select id, title, created_at, owner_id from todos inner join todo_collaborator on id = todo_id and " +
            "collaborator_id = ?1", nativeQuery = true)
    List<ToDo> getByUserId(long userId);
}
