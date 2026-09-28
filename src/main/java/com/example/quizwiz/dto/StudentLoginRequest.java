package com.example.quizwiz.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentLoginRequest {

    @NotBlank
    private String studentCode;

    @NotBlank
    private String password;

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}