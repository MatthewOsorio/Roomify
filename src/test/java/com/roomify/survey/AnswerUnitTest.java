package com.roomify.survey;

import com.roomify.student.Student;
import com.roomify.survery.Answer;
import com.roomify.survery.Question;
import com.roomify.university.University;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class AnswerUnitTest {

    private University university;
    private Student student;
    private Question question;

    @BeforeEach
    void setUp() {
        university = new University("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");

        student = new Student("Matt", "Osorio", "matt@unr.edu", university, "secret", "2000-01-01", 'M');
        student.setId(1L);

        question = new Question("Do you keep your space clean?");
        question.setId(10L);
    }

    @Test
    void constructor() {
        String text = "Yes, always";

        Answer answer = new Answer(text, question, student);

        assertEquals(text, answer.getAnswerText());
        assertEquals(question, answer.getQuestion());
        assertEquals(student, answer.getStudent());
    }

    @Test
    void settersAndGetters() {
        Answer answer = new Answer();

        Question newQuestion = new Question("Do you like noise?");
        newQuestion.setId(5L);



        answer.setAnswerText("Not really");
        answer.setQuestion(newQuestion);
        answer.setStudent(student);

        assertEquals("Not really", answer.getAnswerText());
        assertEquals(newQuestion, answer.getQuestion());
        assertEquals(student, answer.getStudent());
    }

    @Test
    void toString_formatsCorrectly() {
        Question q = new Question("Favorite study time?");
        q.setId(8L);

        Student s = new Student("John", "Doe", "john@unr.edu", university, "123", "2003-05-05", 'M');
        s.setId(7L);

        Answer answer = new Answer("Night", q, s);

        String result = answer.toString();

        assertTrue(result.contains("studentId: 7"));
        assertTrue(result.contains("answerText: Night"));
        assertTrue(result.contains("questionId: 8"));
    }
}
