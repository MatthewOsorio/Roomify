package com.roomify.survery;

import com.roomify.student.Student;
import com.roomify.survery.SurveyDTO.AnswerDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table
@Entity
public class Answer {
    @Id
    @SequenceGenerator(name = "answer_sequence", sequenceName = "answer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_sequence")
    private Long id;
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    public Answer() {
    }

    public Answer(String answerText, Question question, Student student) {
        this.answerText = answerText;
        this.question = question;
        this.student = student;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }    

    @Override
    public String toString() {
        return "studentId: " + (student != null ? student.getId() : null) + "\n" +
                "answerText: " + answerText + "\n" +
                "questionId: " + (question != null ? question.getId() : null) + "\n";
    }
}