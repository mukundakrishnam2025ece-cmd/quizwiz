package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    boolean existsByStudentAndQuiz(Student student, Quiz quiz);
}