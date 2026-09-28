package com.example.quizwiz.controller;

import com.example.quizwiz.dto.QuizRequest;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.QuizService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public Quiz createQuiz(@Valid @RequestBody QuizRequest request) {

        Quiz quiz = new Quiz();

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDuration(request.getDuration());
        quiz.setCreatedAt(LocalDateTime.now());

        return quizService.createQuiz(quiz);
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }
}