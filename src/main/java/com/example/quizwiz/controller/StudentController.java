package com.example.quizwiz.controller;

import com.example.quizwiz.dto.StudentRequest;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentService studentService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Student createStudent(@Valid @RequestBody StudentRequest request) {

        Student student = new Student();

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setRegisterNumber(request.getRegisterNumber());
        student.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return studentService.createStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id, Authentication authentication) {
        Student student = studentService.getStudentById(id);
        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_STUDENT"))) {
            String studentCode = authentication.getName().substring("STUDENT:".length());
            if (!student.getStudentCode().equalsIgnoreCase(studentCode)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Students can only view their own profile.");
            }
        }
        return student;
    }
}