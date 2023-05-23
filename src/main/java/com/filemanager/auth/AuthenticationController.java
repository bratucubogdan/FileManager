package com.filemanager.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://10.100.0.114:3000/")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public void register(@ModelAttribute RegisterRequest registerRequest) throws IOException {
        service.register(registerRequest);;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@ModelAttribute AuthenticationRequest request) throws IOException {

        String jwtToken  = service.authenticate(request).getToken();
        System.out.println(jwtToken);

        return ResponseEntity.ok().body(jwtToken);

    }





}