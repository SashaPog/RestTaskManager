package com.example.resttaskmanager.repository;

import com.example.resttaskmanager.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    State findByName(String name);
    List<State> findByOrderByIdAsc();
}
