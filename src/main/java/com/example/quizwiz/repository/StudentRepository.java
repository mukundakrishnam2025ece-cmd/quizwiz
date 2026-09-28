package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}