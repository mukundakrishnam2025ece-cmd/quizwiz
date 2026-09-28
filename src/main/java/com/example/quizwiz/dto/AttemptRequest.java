package com.example.quizwiz.dto;

import jakarta.validation.constraints.NotNull;

public class AttemptRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long quizId;

    public AttemptRequest() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
}