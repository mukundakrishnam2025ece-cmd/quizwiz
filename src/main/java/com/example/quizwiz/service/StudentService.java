package com.example.quizwiz.service;

import com.example.quizwiz.entity.Student;
import com.example.quizwiz.repository.StudentRepository;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.security.SecureRandom;
import java.time.Year;
import java.util.List;

@Service
public class StudentService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void assignCodesToExistingStudents() {
        List<Student> students = studentRepository.findAll();
        boolean changed = false;
        for (Student student : students) {
            if (student.getStudentCode() == null || student.getStudentCode().isBlank()) {
                student.setStudentCode(generateStudentCode());
                changed = true;
            }
        }
        if (changed) {
            studentRepository.saveAll(students);
        }
    }

    public Student createStudent(Student student) {
        if (student.getStudentCode() == null || student.getStudentCode().isBlank()) {
            student.setStudentCode(generateStudentCode());
        }
        return studentRepository.save(student);
    }

    private String generateStudentCode() {
        String code;
        do {
            code = "IC" + String.format("%02d", Year.now().getValue() % 100)
                    + "BER" + String.format("%04d", RANDOM.nextInt(10_000));
        } while (studentRepository.existsByStudentCode(code));
        return code;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student getByStudentCode(String studentCode) {
        return studentRepository.findByStudentCodeIgnoreCase(studentCode.trim())
                .orElseThrow(() -> new RuntimeException("Student account not found"));
    }
}