package com.example.quizwiz.dto;

public record QuestionViewResponse(Long id, String questionText,
                                   String optionA, String optionB,
                                   String optionC, String optionD) {
}