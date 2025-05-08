package com.roomify.student.StudentDTO;

import com.roomify.university.UniversityDTO.UniversityDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRequestDTOUnitTest {

    @Test
    public void constructor() {
        UniversityDTO universityDTO = new UniversityDTO("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");

        StudentRequestDTO dto = new StudentRequestDTO(
                "Matt",
                "Osorio",
                "matt@unr.edu",
                universityDTO,
                "password123",
                "2002-12-21",
                'M'
        );

        assertEquals("Matt", dto.getFirstName());
        assertEquals("Osorio", dto.getLastName());
        assertEquals("matt@unr.edu", dto.getEmail());
        assertEquals(universityDTO, dto.getUniversity());
        assertEquals("password123", dto.getPassword());
        assertEquals("2002-12-21", dto.getDob());
        assertEquals('M', dto.getSex());
    }

    @Test
    public void defaultConstructor() {
        StudentRequestDTO dto = new StudentRequestDTO();
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertNull(dto.getUniversity());
        assertNull(dto.getPassword());
        assertNull(dto.getDob());
        assertEquals('\0', dto.getSex());
    }
}
