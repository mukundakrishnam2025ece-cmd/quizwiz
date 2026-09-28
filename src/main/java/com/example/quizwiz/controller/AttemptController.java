package com.example.quizwiz.controller;

import com.example.quizwiz.dto.AttemptRequest;
import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.AttemptService;
import com.example.quizwiz.service.StudentService;
import com.example.quizwiz.service.QuizService;
import com.example.quizwiz.dto.SubmitAttemptRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;
    private final StudentService studentService;
    private final QuizService quizService;

    public AttemptController(AttemptService attemptService,
                             StudentService studentService,
                             QuizService quizService) {
        this.attemptService = attemptService;
        this.studentService = studentService;
        this.quizService = quizService;
    }

    @PostMapping("/start")
    public Attempt startAttempt(@Valid @RequestBody AttemptRequest request) {

        Student student = studentService.getStudentById(request.getStudentId());
        Quiz quiz = quizService.getQuizById(request.getQuizId());
        if (attemptService.hasAlreadyAttempted(student, quiz)) {
            throw new RuntimeException("Student has already attempted this quiz");
        }

        Attempt attempt = new Attempt();

        attempt.setStudent(student);
        attempt.setQuiz(quiz);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setScore(0);
        attempt.setStatus("IN_PROGRESS");

        return attemptService.createAttempt(attempt);
    }

    @GetMapping
    public List<Attempt> getAllAttempts() {
        return attemptService.getAllAttempts();
    }

    @GetMapping("/{id}")
    public Attempt getAttemptById(@PathVariable Long id) {
        return attemptService.getAttemptById(id);
    }
    @PostMapping("/{id}/submit")
    public Attempt submitAttempt(
            @PathVariable Long id,
            @RequestBody SubmitAttemptRequest request) {

        return attemptService.submitAttempt(id, request.getAnswers());
    }
    @GetMapping("/quiz/{quizId}/results")
    public List<Attempt> getQuizResults(@PathVariable Long quizId) {

        Quiz quiz = quizService.getQuizById(quizId);

        return attemptService.getResultsByQuiz(quiz);
    }
}