package com.example.resttaskmanager.controller;

import com.example.resttaskmanager.dto.ToDoRequest;
import com.example.resttaskmanager.dto.ToDoResponse;
import com.example.resttaskmanager.mapper.ToDoMapper;
import com.example.resttaskmanager.model.ToDo;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.service.ToDoService;
import com.example.resttaskmanager.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/users/{user_id}/todos")
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;
    private final ToDoMapper toDoMapper;

    @Autowired
    public ToDoController(ToDoService toDoService, UserService userService, ToDoMapper toDoMapper) {
        this.toDoService = toDoService;
        this.userService = userService;
        this.toDoMapper = toDoMapper;
    }


    // Get all user todos
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @GetMapping
    public List<ToDoResponse> getAll(@PathVariable("user_id") long userId) {
        log.info("User is retrieving all todos for user {}", userId);

        return toDoService.getByUserId(userId)
                .stream()
                .map(ToDoResponse::new)
                .toList();
    }

    // Create a new User ToD
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @PostMapping
    public ResponseEntity<ToDoResponse> createToDo(@PathVariable("user_id") long userId,
                                                   @Valid @RequestBody ToDoRequest toDoRequest,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while creating a new ToDo for User {}", userId);
            return ResponseEntity.badRequest().build();
        }

        User owner = userService.readById(userId);

        if (owner == null) {
            log.error("User with id {} not found", userId);
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        ToDo newToDo = toDoMapper.dtoToModel(toDoRequest);
        newToDo.setOwner(owner);

        log.info("Creating a new ToDo for User {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ToDoResponse(toDoService.create(newToDo)));
    }

    // Get User ToD
    @GetMapping("/{todo_id}")
    public ResponseEntity<ToDoResponse> getToDo(@PathVariable("user_id") long userId,
                                                @PathVariable("todo_id") long todoId,
                                                Authentication authentication) {
        ToDo toDo = toDoService.readById(todoId);

        if (toDo == null || userId != toDo.getOwner().getId()) {
            log.info("ToDo with id {} not found", todoId);
            throw new EntityNotFoundException("ToDo with id " + todoId + " not found");
        }

        User user = (User) authentication.getPrincipal();

        if (!isAdmin(user) && !Objects.equals(toDo.getOwner().getId(), user.getId()) &&
                !isCollaborator(toDo.getCollaborators(), user)) {

            log.error("Access denied! User with id {} doesn't have access to this", user.getId());
            throw new AccessDeniedException("Access denied! User with id " + user.getId() + " doesn't have access to this");
        }

        log.info("Retrieving ToDo with id {} for user {}", todoId, userId);
        return ResponseEntity.ok(new ToDoResponse(toDo));
    }

    // Update User ToD
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @PutMapping("/{todo_id}")
    public ResponseEntity<ToDoResponse> updateToDo(@PathVariable("user_id") long userId,
                                                   @PathVariable("todo_id") long todoId,
                                                   @Valid @RequestBody ToDoRequest toDoRequest,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while updating ToDo for User {}", userId);
            return ResponseEntity.badRequest().build();
        }

        ToDo oldToDo = toDoService.readById(todoId);

        if (oldToDo == null || userId != oldToDo.getOwner().getId()) {
            log.info("ToDo for update with id {} not found or User with id {} is not the owner", todoId, userId);
            throw new EntityNotFoundException("ToDo with id " + todoId + " not found or User with id " + userId);
        }

        oldToDo.setTitle(toDoRequest.getTitle());
        oldToDo.setCreatedAt(toDoRequest.getCreatedAt());
        ToDo returnTodo = toDoService.update(oldToDo);

        log.info("Updating ToDo with id {} for user {}", todoId, userId);
        return ResponseEntity.ok().body(new ToDoResponse(returnTodo));
    }

    // Delete User ToD
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @DeleteMapping("/{todo_id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable("user_id") long userId,
                                           @PathVariable("todo_id") long todoId) {

        ToDo toDo = toDoService.readById(todoId);

        if (toDo == null || userId != toDo.getOwner().getId()) {
            log.info("ToDo with id {} not found or user with id {} is not the owner", todoId, userId);
            throw new EntityNotFoundException("ToDo with id " + todoId + " not found or User with id " + userId);
        }

        toDoService.delete(todoId);

        log.info("Deleting ToDo with id {} for user {}", todoId, userId);
        return ResponseEntity.ok().build();
    }

    // Add ToD Collaborator
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @PostMapping("/{todo_id}/collaborators")
    public ResponseEntity<Void> addCollaborator(@PathVariable("user_id") long userId,
                                                @PathVariable("todo_id") long todoId,
                                                @RequestBody Map<String, Long> requestBody) {
        long collaboratorId = requestBody.get("collaborator_id");

        ToDo toDo = toDoService.readById(todoId);

        User collaborator = userService.readById(collaboratorId);

        if (toDo == null || collaborator == null || userId != toDo.getOwner().getId()) {
            String errorMessage =
                    String.format("ToDo with id %s or User with id %s not found or user with id %s is not the owner",
                            todoId, collaboratorId, userId);
            log.info(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        List<User> collaborators = toDo.getCollaborators();

        if (collaborators.contains(collaborator)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        collaborators.add(collaborator);

        toDoService.update(toDo);

        log.info("Added collaborator with id {} to ToDo with id {} for user {}", collaboratorId, todoId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Remove ToD collaborator
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.id")
    @DeleteMapping("/{todo_id}/collaborators")
    public ResponseEntity<Void> removeCollaborator(@PathVariable("user_id") long userId,
                                                   @PathVariable("todo_id") long todoId,
                                                   @RequestBody Map<String, Long> requestBody) {
        long collaboratorId = requestBody.get("collaborator_id");

        ToDo toDo = toDoService.readById(todoId);

        User collaborator = userService.readById(collaboratorId);

        if (toDo == null || collaborator == null || userId != toDo.getOwner().getId()) {
            String errorMessage =
                    String.format("ToDo with id %s or User with id %s not found or user with id %s is not the owner",
                            todoId, collaboratorId, userId);
            log.info(errorMessage);
            throw new EntityNotFoundException(errorMessage);
        }

        List<User> collaborators = toDo.getCollaborators();

        if (!collaborators.contains(collaborator)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        collaborators.remove(collaborator);

        toDoService.update(toDo);

        log.info("Removed collaborator with id {} from ToDo with id {} for user {}", collaboratorId, todoId, userId);
        return ResponseEntity.ok().build();
    }

    private boolean isAdmin(User user) {
        return user.getRole().getName().equals("ADMIN");
    }

    private boolean isCollaborator(List<User> collaborators, User user) {
        return collaborators.stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()));
    }
}
