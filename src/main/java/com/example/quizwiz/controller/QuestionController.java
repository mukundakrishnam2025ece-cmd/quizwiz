package com.example.quizwiz.controller;

import com.example.quizwiz.dto.QuestionRequest;
import com.example.quizwiz.entity.Question;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.service.QuestionService;
import com.example.quizwiz.service.QuizService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;
    private final QuizService quizService;

    public QuestionController(QuestionService questionService,
                              QuizService quizService) {
        this.questionService = questionService;
        this.quizService = quizService;
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public Question createQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequest request) {

        Quiz quiz = quizService.getQuizById(quizId);

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
    public List<Question> getQuestionsByQuiz(@PathVariable Long quizId) {

        quizService.getQuizById(quizId);

        return questionService.getAllQuestions()
                .stream()
                .filter(question ->
                        question.getQuiz().getId().equals(quizId))
                .toList();
    }

    @GetMapping("/questions/{id}")
    public Question getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }
}