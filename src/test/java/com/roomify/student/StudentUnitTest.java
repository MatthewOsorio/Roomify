package com.roomify.student;

import com.roomify.survery.Answer;
import com.roomify.university.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StudentUnitTest {

    private University university;
    private LocalDate dob;

    @BeforeEach
    void setup() {
        university = new University("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");
        dob = LocalDate.of(2000, 1, 15);
    }

    @Test
    void constructorWithId() {
        Student student = new Student(1L, "Matt", "Osorio", "matt@unr.edu", university, "password123", dob, 'M');

        assertEquals(1L, student.getId());
        assertEquals("Matt", student.getFirstName());
        assertEquals("Osorio", student.getLastName());
        assertEquals("matt@unr.edu", student.getEmail());
        assertEquals("password123", student.getPassword());
        assertEquals(university, student.getUniversity());
        assertEquals('M', student.getSex());
        assertEquals(dob, student.getDob());
    }

    @Test
    void constructorWithStringDob() {
        Student student = new Student("Matt", "Osorio", "matt@unr.edu", university, "password123", "2000-01-15", 'M');

        assertEquals("Matt", student.getFirstName());
        assertEquals(LocalDate.of(2000, 1, 15), student.getDob());
    }

    @Test
    void getAge() {
        LocalDate birthday = LocalDate.now().minusYears(22);
        Student student = new Student(1L, "Matt", "Osorio", "matt@unr.edu", university, "password123", birthday, 'M');

        assertEquals(22, student.getAge());
    }

    @Test
    void settersAndGetters() {
        Student student = new Student();
        student.setId(10L);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setEmail("alice@unr.edu");
        student.setPassword("secure");
        student.setSex('F');
        student.setUniversity(university);

        assertEquals(10L, student.getId());
        assertEquals("Alice", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("alice@unr.edu", student.getEmail());
        assertEquals("secure", student.getPassword());
        assertEquals('F', student.getSex());
        assertEquals(university, student.getUniversity());
    }

    @Test
    void addAnswer() {
        Student student = new Student("Matt", "Osorio", "matt@unr.edu", university, "pass", "2000-01-01", 'M');
        Answer answer = new Answer();
        student.addAnswer(answer);

        assertEquals(1, student.getAnswers().size());
        assertEquals(student, answer.getStudent());
    }

    @Test
    void toStrings() {
        Student student = new Student("Matt", "Osorio", "matt@unr.edu", university, "pass", "2000-01-01", 'M');
        student.setId(1L);

        String toString = student.toString();
        assertTrue(toString.contains("Matt"));
        assertTrue(toString.contains("UNR"));
        assertTrue(toString.contains("matt@unr.edu"));
    }
}
