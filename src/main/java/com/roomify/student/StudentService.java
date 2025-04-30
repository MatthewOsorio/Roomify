package com.roomify.student;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public void addStudent(Student student) {
        Optional<Student> existingStudent = studentRepository.findStudentByEmail(student.getEmail());

        if (existingStudent.isPresent()) {
            throw new IllegalStateException("Student with ID " + student.getId() + " already exists.");
        }
        studentRepository.save(student);
    }

}
