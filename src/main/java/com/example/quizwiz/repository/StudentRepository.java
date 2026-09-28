package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

	boolean existsByStudentCode(String studentCode);

	Optional<Student> findByStudentCodeIgnoreCase(String studentCode);
}