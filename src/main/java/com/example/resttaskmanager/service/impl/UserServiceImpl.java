package com.example.resttaskmanager.service.impl;

import com.example.resttaskmanager.exception.NullEntityReferenceException;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.repository.UserRepository;
import com.example.resttaskmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User readByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public User update(User user) {
        if (user != null) {
            User foundUser = readById(user.getId());
            if(!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readByEmail(username);
    }
}
