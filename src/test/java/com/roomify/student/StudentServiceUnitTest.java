package com.roomify.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.roomify.student.StudentDTO.StudentRequestDTO;
import com.roomify.university.University;
import com.roomify.university.UniversityRepository;
import com.roomify.university.UniversityDTO.UniversityDTO;

@ExtendWith(MockitoExtension.class)
public class StudentServiceUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentRequestDTO studentDTO;
    private UniversityDTO universityDTO;

    @BeforeEach
    void setup(){
        universityDTO = new UniversityDTO("University of Nevada, Reno", "Reno", "1664 N. Virginia Street", 89557, "NV");
        studentDTO = new StudentRequestDTO("Matthew", "Osorio", "matthewosorio@unr.edu", universityDTO, "password1", "2002-12-21", 'M');
    }

    @Test
    void addStudent_createsNewStudentAndUniversity() {
        when(studentRepository.findStudentByEmailIgnoreCase(studentDTO.getEmail()))
            .thenReturn(Optional.empty());
    
        when(universityRepository.findByNameIgnoreCase(studentDTO.getUniversity().getName()))
            .thenReturn(Optional.empty());
        
        University newUniversity = new University(studentDTO.getUniversity());
        when(universityRepository.save(any())).thenReturn(newUniversity);

        studentService.addStudent(studentDTO);
    
        verify(studentRepository).findStudentByEmailIgnoreCase(studentDTO.getEmail());
        verify(universityRepository).findByNameIgnoreCase(studentDTO.getUniversity().getName());
        verify(universityRepository).save(any(University.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void addStudent_createNewStudentWithExistingUniversity() {
        when(studentRepository.findStudentByEmailIgnoreCase(studentDTO.getEmail()))
        .thenReturn(Optional.empty());

        University existingUniversity = new University(studentDTO.getUniversity());
        when(universityRepository.findByNameIgnoreCase(studentDTO.getUniversity().getName()))
        .thenReturn(Optional.of(existingUniversity));

        studentService.addStudent(studentDTO);

        verify(studentRepository).findStudentByEmailIgnoreCase(studentDTO.getEmail());
        verify(universityRepository).findByNameIgnoreCase(studentDTO.getUniversity().getName());
        verify(universityRepository, never()).save(any());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void addStudent_studentAlreadyExists() {
        when(studentRepository.findStudentByEmailIgnoreCase(studentDTO.getEmail()))
        .thenReturn(Optional.of(new Student()));

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> studentService.addStudent(studentDTO)
        );

        assertEquals(String.format("Student with email %s already exists.", studentDTO.getEmail()), exception.getMessage());
        verify(studentRepository, never()).save(any());
        verify(universityRepository, never()).save(any());
    }
}