package com.roomify.student;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.PostgresTestContainer;
import com.roomify.student.StudentDTO.StudentRequestDTO;
import com.roomify.university.University;
import com.roomify.university.UniversityRepository;
import com.roomify.university.UniversityDTO.UniversityDTO;

import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentControllerIntegrationTest extends PostgresTestContainer{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Test
    public void addStudent_ValidInput() throws Exception {

        UniversityDTO universityDTO = new UniversityDTO("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");
        StudentRequestDTO studentDTO = new StudentRequestDTO(
            "Matthew", "Osorio", "matthewosorio@unr.edu", universityDTO, "password", "2002-12-21", 'M'
        );

        mockMvc.perform(post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(studentDTO)))
                .andExpect(status().isOk());

    }

    @Test
    public void addStudent_DuplicateEmail() throws Exception {
        University university = new University("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");
        universityRepository.save(university); 

        UniversityDTO universityDTO = new UniversityDTO("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");
        StudentRequestDTO studentDTO = new StudentRequestDTO(
            "Matthew", "Osorio", "matthewosorio@unr.edu", universityDTO, "password", "2002-12-21", 'M'
        );

        studentRepository.save(new Student(
            studentDTO.getFirstName(), 
            studentDTO.getLastName(), 
            studentDTO.getEmail(), 
            university, 
            studentDTO.getPassword(), 
            studentDTO.getDob(), 
            studentDTO.getSex()));

        mockMvc.perform(post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(studentDTO)))
                .andExpect(status().isConflict());
    }
}
