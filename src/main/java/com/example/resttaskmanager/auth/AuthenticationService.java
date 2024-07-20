package com.example.resttaskmanager.auth;

import com.example.resttaskmanager.config.JwtService;
import com.example.resttaskmanager.dto.UserRequest;
import com.example.resttaskmanager.mapper.UserMapper;
import com.example.resttaskmanager.model.User;
import com.example.resttaskmanager.service.RoleService;
import com.example.resttaskmanager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public User register(UserRequest request) {
        return userService.create(userMapper.dtoToModel(request));
    }

    public AuthenticationResponce authenticate(AuthenticationRequest request) {
        User foundUser = userService.readByEmail(request.getEmail());

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(foundUser.getEmail(), foundUser.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e){
            System.out.println(e);
        }

        var jwt = jwtService.generateToken(foundUser);

        return new AuthenticationResponce(jwt);
    }
}
