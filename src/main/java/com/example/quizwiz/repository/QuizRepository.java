package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Quiz;
import com.example.quizwiz.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

	List<Quiz> findByOwner(Faculty owner);

}