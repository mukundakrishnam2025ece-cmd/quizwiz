package com.example.quizwiz.controller;

import com.example.quizwiz.dto.AttemptRequest;
import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.AttemptService;
import com.example.quizwiz.service.StudentService;
import com.example.quizwiz.service.QuizService;
import com.example.quizwiz.service.FacultyService;
import com.example.quizwiz.dto.SubmitAttemptRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;
    private final StudentService studentService;
    private final QuizService quizService;
    private final FacultyService facultyService;

    public AttemptController(AttemptService attemptService,
                             StudentService studentService,
                             QuizService quizService,
                             FacultyService facultyService) {
        this.attemptService = attemptService;
        this.studentService = studentService;
        this.quizService = quizService;
        this.facultyService = facultyService;
    }

    @PostMapping("/start")
    public Attempt startAttempt(@Valid @RequestBody AttemptRequest request, Authentication authentication) {

        Student student = currentStudent(authentication);
        if (!student.getId().equals(request.getStudentId())) {
            throw new AccessDeniedException("You can only start an attempt for your own account.");
        }
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
    public List<Attempt> getAllAttempts(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_FACULTY"))) {
            return attemptService.getAttemptsForQuizzes(
                    quizService.getQuizzesByOwner(facultyService.getByEmail(authentication.getName())));
        }
        return attemptService.getAttemptsByStudent(currentStudent(authentication));
    }

    @GetMapping("/{id}")
    public Attempt getAttemptById(@PathVariable Long id, Authentication authentication) {
        Attempt attempt = attemptService.getAttemptById(id);
        assertOwnAttempt(attempt, authentication);
        return attempt;
    }
    @PostMapping("/{id}/submit")
    public Attempt submitAttempt(
            @PathVariable Long id,
            @RequestBody SubmitAttemptRequest request,
            Authentication authentication) {

        assertOwnAttempt(attemptService.getAttemptById(id), authentication);
        return attemptService.submitAttempt(id, request.getAnswers());
    }
    @GetMapping("/quiz/{quizId}/results")
    public List<Attempt> getQuizResults(@PathVariable Long quizId, Authentication authentication) {

        Quiz quiz = quizService.getQuizById(quizId);
        quizService.requireOwnedBy(quiz, facultyService.getByEmail(authentication.getName()));

        return attemptService.getResultsByQuiz(quiz);
    }

    private Student currentStudent(Authentication authentication) {
        String studentCode = authentication.getName().substring("STUDENT:".length());
        return studentService.getByStudentCode(studentCode);
    }

    private void assertOwnAttempt(Attempt attempt, Authentication authentication) {
        if (!attempt.getStudent().getStudentCode().equalsIgnoreCase(currentStudent(authentication).getStudentCode())) {
            throw new AccessDeniedException("You can only access your own quiz attempts.");
        }
    }
}