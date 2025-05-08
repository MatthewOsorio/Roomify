package com.roomify.matching;

import static org.junit.jupiter.api.Assertions.*;

import com.roomify.student.Student;
import com.roomify.university.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MatchUnitTest {

    private University university;
    private Student student;
    private Student updatedStudent;

    @BeforeEach
    void setUp() {
        university = new University("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");

        student = new Student("Matt", "Osorio", "matt@unr.edu", university, "password", "2000-01-01", 'M');
        student.setId(1L);

        updatedStudent = new Student("Updated", "Student", "update@unr.edu", university, "password", "2001-01-01", 'M');
        updatedStudent.setId(2L);
    }

    @Test
    void constructor() {
        Match match = new Match(student, 0.92);

        assertEquals(student, match.getStudent());
        assertEquals(0.92, match.getScore(), 0.0001);
    }

    @Test
    void setters() {
        Match match = new Match(student, 0.5);

        match.setStudent(updatedStudent);
        match.setScore(0.88);

        assertEquals(updatedStudent, match.getStudent());
        assertEquals(0.88, match.getScore(), 0.0001);
    }

    @Test
    void getScore() {
        Match match = new Match(student, 0.75);
        assertEquals(0.75, match.getScore(), 0.0001);
    }
}
