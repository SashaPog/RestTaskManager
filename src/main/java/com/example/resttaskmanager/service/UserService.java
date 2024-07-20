package com.example.resttaskmanager.service;

import com.example.resttaskmanager.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(User user);
    User readById(long id);
    User readByEmail(String email);
    User update(User user);
    void delete(long id);
    List<User> getAll();
}
