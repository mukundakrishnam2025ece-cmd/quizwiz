package com.example.quizwiz.dto;

import java.util.Map;

public class SubmitAttemptRequest {

    private Map<Long, String> answers;

    public SubmitAttemptRequest() {
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }
}