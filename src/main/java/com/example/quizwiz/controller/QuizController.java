package com.example.quizwiz.controller;

import com.example.quizwiz.dto.QuizRequest;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.QuizService;
import com.example.quizwiz.service.FacultyService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final FacultyService facultyService;

    public QuizController(QuizService quizService, FacultyService facultyService) {
        this.quizService = quizService;
        this.facultyService = facultyService;
    }

    @PostMapping
    public Quiz createQuiz(@Valid @RequestBody QuizRequest request, Authentication authentication) {

        Quiz quiz = new Quiz();

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDuration(request.getDuration());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setOwner(facultyService.getByEmail(authentication.getName()));

        return quizService.createQuiz(quiz);
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/mine")
    public List<Quiz> getMyQuizzes(Authentication authentication) {
        return quizService.getQuizzesByOwner(facultyService.getByEmail(authentication.getName()));
    }

    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id, Authentication authentication) {
        quizService.deleteOwnedQuiz(id, facultyService.getByEmail(authentication.getName()));
    }
}