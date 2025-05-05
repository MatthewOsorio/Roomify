package com.roomify.survery;

import java.util.HashSet;
import java.util.Set;

import com.roomify.survery.SurveyDTO.QuestionDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = "questionText")
)
@Entity
public class Question {
    @Id
    @SequenceGenerator(name = "question_sequence", sequenceName = "question_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_sequence")
    private Long id;

    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();
    
    @Column(nullable = false)
    private String questionText;

    public Question() {
    }

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public Question(QuestionDTO questionDTO) {
        this.id = questionDTO.getId();
        this.questionText = questionDTO.getQuestionText();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    @Override
    public String toString() {
        return id + ": " + questionText;
    }
}
