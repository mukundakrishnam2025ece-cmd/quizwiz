package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Attempt;
import com.example.quizwiz.entity.Student;
import com.example.quizwiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    boolean existsByStudentAndQuiz(Student student, Quiz quiz);
    List<Attempt> findByQuiz(Quiz quiz);
    List<Attempt> findByStudent(Student student);
    List<Attempt> findByQuizIn(List<Quiz> quizzes);
    void deleteByQuiz(Quiz quiz);
}