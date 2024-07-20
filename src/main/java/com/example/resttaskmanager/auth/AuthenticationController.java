package com.example.resttaskmanager.auth;

import com.example.resttaskmanager.dto.UserRequest;
import com.example.resttaskmanager.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/sign-up")
    public ResponseEntity<String> register(@RequestBody @Validated UserRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Invalid field for user");
        }
        User registeredUser = service.register(request);
        return registeredUser != null ?
                ResponseEntity.ok("You have successfully registered with email " + request.getEmail() + "!")
                :
                ResponseEntity.badRequest().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody @Validated AuthenticationRequest request,
                                                               BindingResult bindingResult){
        System.out.println("request= " + request);
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(new AuthenticationResponce("Invalid username or password"));
        }
        return ResponseEntity.ok(service.authenticate(request));
    }
}
