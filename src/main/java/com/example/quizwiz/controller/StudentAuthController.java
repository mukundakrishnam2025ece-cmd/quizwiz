package com.example.quizwiz.controller;

import com.example.quizwiz.dto.StudentLoginRequest;
import com.example.quizwiz.dto.StudentRequest;
import com.example.quizwiz.dto.StudentResponse;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/student")
public class StudentAuthController {

    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public StudentAuthController(StudentService studentService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 SecurityContextRepository securityContextRepository) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> register(@Valid @RequestBody StudentRequest request,
                                                    HttpServletRequest httpRequest,
                                                    HttpServletResponse httpResponse) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setRegisterNumber(request.getRegisterNumber());
        student.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        student = studentService.createStudent(student);
        signIn(student.getStudentCode(), request.getPassword(), httpRequest, httpResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(student));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody StudentLoginRequest request,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) {
        try {
            signIn(request.getStudentCode(), request.getPassword(), httpRequest, httpResponse);
            return ResponseEntity.ok(toResponse(studentService.getByStudentCode(request.getStudentCode())));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Invalid student ID or password."
            ));
        }
    }

    @GetMapping("/me")
    public StudentResponse currentStudent(@AuthenticationPrincipal UserDetails userDetails) {
        String studentCode = userDetails.getUsername().substring("STUDENT:".length());
        return toResponse(studentService.getByStudentCode(studentCode));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request,
                                      HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
        return Map.of("message", "Signed out successfully.");
    }

    private void signIn(String studentCode, String password,
                        HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated("STUDENT:" + studentCode.trim(), password));
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

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getEmail(),
                student.getRegisterNumber(), student.getStudentCode(), "STUDENT");
    }
}