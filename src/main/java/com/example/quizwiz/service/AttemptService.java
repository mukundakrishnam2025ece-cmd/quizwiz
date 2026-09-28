package com.example.quizwiz.service;

import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;

    public AttemptService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
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
}