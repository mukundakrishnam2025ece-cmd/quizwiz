package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

}