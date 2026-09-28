package com.example.quizwiz.service;

import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.entity.Question;
import com.example.quizwiz.repository.AttemptRepository;
import com.example.quizwiz.repository.QuestionRepository;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.entity.Quiz;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    public AttemptService(AttemptRepository attemptRepository,
                          QuestionRepository questionRepository) {
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
    }

    public Attempt createAttempt(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public Attempt getAttemptById(Long id) {
        return attemptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));
    }

    public Attempt submitAttempt(Long id, Map<Long, String> answers) {

        Attempt attempt = getAttemptById(id);

        if ("SUBMITTED".equals(attempt.getStatus())) {
            throw new RuntimeException("Attempt already submitted");
        }

        LocalDateTime now = LocalDateTime.now();

        long elapsedMinutes =
                java.time.Duration.between(attempt.getStartedAt(), now).toMinutes();

        if (elapsedMinutes > attempt.getQuiz().getDuration()) {

            attempt.setSubmittedAt(now);
            attempt.setStatus("EXPIRED");

            return attemptRepository.save(attempt);
        }

        List<Question> questions =
                questionRepository.findAll()
                        .stream()
                        .filter(question ->
                                question.getQuiz().getId()
                                        .equals(attempt.getQuiz().getId()))
                        .toList();

        int score = 0;

        for (Question question : questions) {

            String answer = answers.get(question.getId());

            if (answer != null &&
                    answer.equalsIgnoreCase(question.getCorrectOption())) {
                score++;
            }
        }

        attempt.setScore(score);
        attempt.setSubmittedAt(now);
        attempt.setStatus("SUBMITTED");

        return attemptRepository.save(attempt);
    }
    public List<Attempt> getResultsByQuiz(Quiz quiz) {
        return attemptRepository.findByQuiz(quiz);
    }
}