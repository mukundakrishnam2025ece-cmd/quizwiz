package com.example.quizwiz.controller;

import com.example.quizwiz.dto.QuestionRequest;
import com.example.quizwiz.dto.QuestionViewResponse;
import com.example.quizwiz.entity.Question;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.QuestionService;
import com.example.quizwiz.service.QuizService;
import com.example.quizwiz.service.FacultyService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;
    private final QuizService quizService;
    private final FacultyService facultyService;

    public QuestionController(QuestionService questionService,
                              QuizService quizService,
                              FacultyService facultyService) {
        this.questionService = questionService;
        this.quizService = quizService;
        this.facultyService = facultyService;
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public Question createQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequest request,
            Authentication authentication) {

        Quiz quiz = quizService.getQuizById(quizId);
        quizService.requireOwnedBy(quiz, facultyService.getByEmail(authentication.getName()));

        Question question = new Question();

        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectOption(request.getCorrectOption());
        question.setQuiz(quiz);

        return questionService.createQuestion(question);
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public List<QuestionViewResponse> getQuestionsByQuiz(@PathVariable Long quizId) {

        quizService.getQuizById(quizId);

        return questionService.getAllQuestions()
                .stream()
                .filter(question ->
                        question.getQuiz().getId().equals(quizId))
                .map(this::toViewResponse)
                .toList();
    }

    @GetMapping("/questions/{id}")
    public QuestionViewResponse getQuestionById(@PathVariable Long id) {
        return toViewResponse(questionService.getQuestionById(id));
    }

    private QuestionViewResponse toViewResponse(Question question) {
        return new QuestionViewResponse(question.getId(), question.getQuestionText(),
                question.getOptionA(), question.getOptionB(), question.getOptionC(), question.getOptionD());
    }
}