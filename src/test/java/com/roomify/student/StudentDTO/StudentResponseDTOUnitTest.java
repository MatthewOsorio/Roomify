package com.roomify.student.StudentDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.roomify.student.Student;
import com.roomify.university.University;
import com.roomify.university.UniversityDTO.UniversityDTO;

import org.junit.jupiter.api.Test;

public class StudentResponseDTOUnitTest {

    @Test
    void constructorFromStudent() {
        University university = new University("University of Nevada, Reno", "Reno", "1664 N. Virginia St", 89557, "NV");
        Student student = new Student("Alice", "Smith", "alice@unr.edu", university, "password123", "2000-01-01", 'F');
        student.setId(1L);

        StudentResponseDTO dto = new StudentResponseDTO(student);

        assertEquals(1L, dto.getId());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());

        UniversityDTO universityDTO = dto.getUniversity();
        assertNotNull(universityDTO);
        assertEquals("University of Nevada, Reno", universityDTO.getName());

        assertEquals(LocalDate.now().getYear() - 2000, dto.getAge()); // approximate age check
    }

    @Test
    void defaultConstructor() {
        StudentResponseDTO dto = new StudentResponseDTO();

        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getUniversity());
        assertNull(dto.getAge());
    }
}
