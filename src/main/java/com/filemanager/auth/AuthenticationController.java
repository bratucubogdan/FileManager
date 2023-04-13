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
public class AuthenticationController {

    private final AuthenticationService service;

    private final Token token;



    
    public void Token(Model model) {
        String hope = token.getToken();
        model.addAttribute("token", hope);

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
    public void authenticate(@ModelAttribute("authRequest") AuthenticationRequest request, HttpServletResponse response) throws IOException {
        // Authenticate the user and get the JWT token
        token.setToken(service.authenticate(request).getToken());
        String jwtToken = token.getToken();
        System.out.println(service.authenticate(request).getToken());
        // Set the JWT token as a cookie in the response
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setMaxAge(3600); // set the cookie to expire in 1 hour
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // Redirect the user to the file search page
        response.sendRedirect("/api/v1/file/search");
    }





}