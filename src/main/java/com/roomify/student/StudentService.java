package com.roomify.student;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roomify.student.StudentDTO.StudentRequestDTO;
import com.roomify.university.University;
import com.roomify.university.UniversityRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, UniversityRepository universityRepository) {
        this.studentRepository = studentRepository;
        this.universityRepository = universityRepository;
    }

    public void addStudent(StudentRequestDTO student) {
        Optional<Student> existingStudent = studentRepository.findStudentByEmailIgnoreCase(student.getEmail());

        if (existingStudent.isPresent()) {
            throw new IllegalStateException("Student with email " + student.getEmail() + " already exists.");
        }
        
        University university = universityRepository.findByNameIgnoreCase(student.getUniversity().getName())
        .orElseGet(() -> {
            University newUniversity = new University(student.getUniversity());
            University savedUniversity = universityRepository.save(newUniversity);
            System.out.println("Saved university ID: " + savedUniversity.getId());
            return savedUniversity;
        });
    
        System.out.println("Using university ID: " + university.getId());

        Student newStudent = new Student(student.getFirstName(), student.getLastName(), student.getEmail(), university, student.getPassword(), student.getDob(), student.getSex());
        studentRepository.save(newStudent);
    }
}
