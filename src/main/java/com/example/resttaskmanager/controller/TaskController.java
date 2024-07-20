package com.example.resttaskmanager.controller;

import com.example.resttaskmanager.dto.TaskRequest;
import com.example.resttaskmanager.dto.TaskResponse;
import com.example.resttaskmanager.mapper.TaskMapper;
import com.example.resttaskmanager.model.Task;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.service.TaskService;
import com.example.resttaskmanager.service.ToDoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class TaskController {

    private final ToDoService toDoService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/api/users/{userId}/todos/{todoId}/tasks")
    public List<TaskResponse> getAllTasks(@PathVariable long userId, @PathVariable long todoId, Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if (isAdmin) {
            return taskService.getAll().stream()
                    .map(TaskResponse::new)
                    .collect(Collectors.toList());
        }

        if (!loggedUser.getId().equals(userId) || !toDoService.readById(todoId).getOwner().getId().equals(userId)){
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        } else {
            return taskService.getByTodoId(todoId).stream()
                    .map(TaskResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/api/users/{userId}/todos/{todoId}/tasks/{task_id}")
    public TaskResponse getTaskById(@PathVariable Long task_id, @PathVariable Long todoId, @PathVariable Long userId,
                                    Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if (!isAdmin && !taskService.readById(task_id).getTodo().getId().equals(todoId) ||
                !toDoService.readById(todoId).getOwner().getId().equals(userId)){
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        } else {
            Task task = taskService.readById(task_id);
            return new TaskResponse(task);
        }
    }

    @PostMapping("/api/users/{userId}/todos/{todoId}/tasks")
    public TaskResponse createTask(@RequestBody @Validated TaskRequest request,
                                   @PathVariable long todoId, @PathVariable Long userId,
                                   BindingResult bindingResult, Authentication authentication){
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if(isAdmin){
            Task task = taskMapper.dtoToModel(request);
            task.setTodo(toDoService.readById(todoId));
            return new TaskResponse(taskService.create(task));
        }

        if (!loggedUser.getId().equals(userId) || !toDoService.readById(todoId).getOwner().getId().equals(userId)){
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        } else {
            Task task = taskMapper.dtoToModel(request);
            task.setTodo(toDoService.readById(todoId));
            return new TaskResponse(taskService.create(task));
        }
    }


    @PutMapping("/api/users/{userId}/todos/{todoId}/tasks/{taskId}")
    public TaskResponse updateTaskById(@PathVariable long taskId, @RequestBody @Validated TaskRequest request,
                                       @PathVariable long todoId, @PathVariable long userId,
                                       BindingResult bindingResult, Authentication authentication) {

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if(isAdmin){
            Task updatedTask = taskMapper.dtoToModel(request);
            updatedTask.setId(taskId);
            updatedTask.setTodo(toDoService.readById(todoId));
            return new TaskResponse(taskService.update(updatedTask));
        }

        if (!loggedUser.getId().equals(userId) || !toDoService.readById(todoId).getOwner().getId().equals(userId)) {
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        } else {
            Task updatedTask = taskMapper.dtoToModel(request);
            updatedTask.setId(taskId);
            updatedTask.setTodo(toDoService.readById(todoId));
            return new TaskResponse(taskService.update(updatedTask));
        }
    }

    @DeleteMapping("/api/users/{userId}/todos/{todoId}/tasks/{taskId}")
    public ResponseEntity<String> DeleteTaskById(@PathVariable Long taskId, @PathVariable long todoId,
                                                 @PathVariable long userId, Authentication authentication) {

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if(isAdmin){
            taskService.delete(taskId);
            return ResponseEntity.ok("Task with id = " + taskId + " has been deleted");
        }

        if (!loggedUser.getId().equals(userId) || !toDoService.readById(todoId).getOwner().getId().equals(userId)) {
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        } else {
            taskService.delete(taskId);
            return ResponseEntity.ok("Task with id = " + taskId + " has been deleted");
        }
    }
}
