package com.example.quizwiz.service;

import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.entity.Faculty;
import com.example.quizwiz.repository.AttemptRepository;
import com.example.quizwiz.repository.QuestionRepository;
import com.example.quizwiz.repository.QuizRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;

    public QuizService(QuizRepository quizRepository,
                       QuestionRepository questionRepository,
                       AttemptRepository attemptRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<Quiz> getQuizzesByOwner(Faculty owner) {
        return quizRepository.findByOwner(owner);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public Quiz requireOwnedBy(Quiz quiz, Faculty faculty) {
        if (quiz.getOwner() == null || !quiz.getOwner().getId().equals(faculty.getId())) {
            throw new AccessDeniedException("You can only manage quizzes you created.");
        }
        return quiz;
    }

    @Transactional
    public void deleteOwnedQuiz(Long id, Faculty faculty) {
        Quiz quiz = requireOwnedBy(getQuizById(id), faculty);
        attemptRepository.deleteByQuiz(quiz);
        questionRepository.deleteByQuiz(quiz);
        quizRepository.delete(quiz);
    }
}