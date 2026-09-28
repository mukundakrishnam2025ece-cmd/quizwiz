package com.example.quizwiz.service;

import com.example.quizwiz.entity.Faculty;
import com.example.quizwiz.repository.FacultyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyService(FacultyRepository facultyRepository,
                          PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Faculty register(String name, String email, String password) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (facultyRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        Faculty faculty = new Faculty(
                name.trim(),
                normalizedEmail,
                passwordEncoder.encode(password)
        );
        return facultyRepository.save(faculty);
    }

    public Faculty getByEmail(String email) {
        return facultyRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Faculty account not found."));
    }
}