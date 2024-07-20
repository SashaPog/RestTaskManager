package com.example.resttaskmanager.controller;

import com.example.resttaskmanager.dto.UserRequest;
import com.example.resttaskmanager.dto.UserResponse;
import com.example.resttaskmanager.mapper.UserMapper;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable Long userId) {
        User user = userService.readById(userId);

        return new UserResponse(user);
    }

    /**
     * Only User with ADMIN role can create new users
     *
     * @param request
     * @param bindingResult
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public UserResponse createUser(@RequestBody @Validated UserRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        return new UserResponse(userService.create(userMapper.dtoToModel(request)));
    }


    /**
     * If user has ADMIN role then it can modify any other user
     * If user has USER role then it can modify only itself
     *
     * @param userId
     * @param request
     * @param bindingResult
     * @param authentication
     * @return
     */
    @PutMapping("/{userId}")
    public UserResponse updateUserById(@PathVariable Long userId, @RequestBody @Validated UserRequest request,
                                       BindingResult bindingResult, Authentication authentication) {
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult.getAllErrors().toString());
        }

        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if (!isAdmin && (!loggedUser.getId().equals(userId)
                || (request.getRoleName() != null && request.getRoleName().equals(loggedUser.getRole().getName())))) {
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        }

        User convertedUser = userMapper.dtoToModel(request);
        convertedUser.setId(userId);

        return new UserResponse(userService.update(convertedUser));
    }

    /**
     * If user has ADMIN role then it can delete any other user
     * If user has USER role then it can delete only itself
     *
     * @param userId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> DeleteUserById(@PathVariable Long userId, Authentication authentication) {
        User loggedUser = (User) authentication.getPrincipal();

        boolean isAdmin = loggedUser.getRole().getName().equals("ADMIN");

        if (!isAdmin && !loggedUser.getId().equals(userId)) {
            throw new AccessDeniedException(
                    "Access denied. Users with role = " + loggedUser.getRole().getName() +
                            " can only access their own data."
            );
        }

        userService.delete(userId);

        return ResponseEntity.ok("User with id = " + userId + " has been deleted");
    }
}
