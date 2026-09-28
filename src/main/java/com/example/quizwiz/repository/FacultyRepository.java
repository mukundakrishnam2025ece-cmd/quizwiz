package com.example.quizwiz.repository;

import com.example.quizwiz.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}