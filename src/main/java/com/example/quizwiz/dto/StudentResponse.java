package com.example.quizwiz.dto;

public record StudentResponse(Long id, String name, String email,
                              String registerNumber, String studentCode, String role) {
}