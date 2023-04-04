package com.filemanager.auth;

import com.filemanager.file.FileModel;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    public String getToken() {
        return token;
    }

    private String token;

    @ModelAttribute
    public void setRequestHeader(HttpServletResponse response) {
        response.setHeader("Authorization", "Bearer " + token);
    }

    @GetMapping("/registerForm")
    public ModelAndView showRegisterForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registrationForm");
        modelAndView.addObject("registerRequest", new RegisterRequest());
        return modelAndView;
    }

    @PostMapping("/register")
    public void register(
            @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            HttpServletResponse response
    ) throws IOException {
        service.register(registerRequest);
        response.sendRedirect("/api/v1/auth/login");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("authRequest", new AuthenticationRequest());
        return modelAndView;
    }

    @PostMapping("/authenticate")
    public void authenticate(
            @ModelAttribute("authRequest") AuthenticationRequest request,
            HttpServletResponse response
    ) throws IOException {
        token = service.authenticate(request).getToken();
        System.out.println(token);

        response.sendRedirect("/search");
    }


}