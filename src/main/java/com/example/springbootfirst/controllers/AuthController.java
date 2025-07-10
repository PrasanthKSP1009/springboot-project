package com.example.springbootfirst.controllers;

import com.example.springbootfirst.models.JwtResponse;
import com.example.springbootfirst.models.RegisterDetails;
import com.example.springbootfirst.models.UserDetailsDto;
import com.example.springbootfirst.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserDetailsDto register) {
        return authService.addNewEmployee(register);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody RegisterDetails login) {
        String userName = login.getUserName();

        // 1. Authenticate and generate token
        String token = authService.authenticate(login);

        // 2. Fetch full user with roles from DB
        RegisterDetails dbUser = authService.getUserByUsername(userName);

        // 3. Extract roles
        Set<String> roles = Optional.ofNullable(dbUser.getRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

        // 4. Return token + username + roles
        JwtResponse response = new JwtResponse(token, userName, roles);
        return ResponseEntity.ok(response);
    }

}

