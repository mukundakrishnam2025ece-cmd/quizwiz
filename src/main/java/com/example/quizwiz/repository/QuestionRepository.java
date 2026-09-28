package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Question;
import com.example.quizwiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	void deleteByQuiz(Quiz quiz);
}