package com.gestusers.gestorUsers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestusers.gestorUsers.dto.JwtResponse;
import com.gestusers.gestorUsers.dto.LoginRequest;
import com.gestusers.gestorUsers.dto.RegisterRequest;
import com.gestusers.gestorUsers.model.User;
import com.gestusers.gestorUsers.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@Valid @RequestBody LoginRequest loginRequest){
        JwtResponse jwtResponse = authService.login(loginRequest);
        return ResponseEntity.ok(jwtResponse);        
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        User user = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


}
