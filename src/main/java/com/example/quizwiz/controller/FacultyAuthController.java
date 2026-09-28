package com.example.quizwiz.controller;

import com.example.quizwiz.dto.FacultyLoginRequest;
import com.example.quizwiz.dto.FacultyRegisterRequest;
import com.example.quizwiz.dto.FacultyResponse;
import com.example.quizwiz.entity.Faculty;
import com.example.quizwiz.service.FacultyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/faculty")
public class FacultyAuthController {

    private final FacultyService facultyService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public FacultyAuthController(FacultyService facultyService,
                                 AuthenticationManager authenticationManager,
                                 SecurityContextRepository securityContextRepository) {
        this.facultyService = facultyService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody FacultyRegisterRequest request,
                                      HttpServletRequest httpRequest,
                                      HttpServletResponse httpResponse) {
        Faculty faculty = facultyService.register(
            request.getName(), request.getEmail(), request.getPassword());
        signIn(faculty.getEmail(), request.getPassword(), httpRequest, httpResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(faculty));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody FacultyLoginRequest request,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {
        try {
            signIn(request.getEmail(), request.getPassword(), httpRequest, httpResponse);
            return ResponseEntity.ok(toResponse(facultyService.getByEmail(request.getEmail())));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Invalid faculty email or password."
            ));
        }
    }

    @GetMapping("/me")
    public FacultyResponse currentFaculty(@AuthenticationPrincipal UserDetails userDetails) {
        return toResponse(facultyService.getByEmail(userDetails.getUsername()));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request,
                                      HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return Map.of("message", "Signed out successfully.");
    }

    private void signIn(String email, String password,
                        HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        if (request.getSession(false) == null) {
            request.getSession(true);
        } else {
            request.changeSessionId();
        }
        securityContextRepository.saveContext(context, request, response);
    }

    private FacultyResponse toResponse(Faculty faculty) {
        return new FacultyResponse(faculty.getId(), faculty.getName(), faculty.getEmail(), "FACULTY");
    }
}