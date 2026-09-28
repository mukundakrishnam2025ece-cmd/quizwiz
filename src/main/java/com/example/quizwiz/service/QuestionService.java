package com.example.quizwiz.service;

import com.example.quizwiz.entity.Question;
import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public void deleteByQuiz(Quiz quiz) {
        questionRepository.deleteByQuiz(quiz);
    }
}