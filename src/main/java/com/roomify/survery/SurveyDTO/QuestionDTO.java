package com.roomify.survery.SurveyDTO;

import com.roomify.survery.Question;

public class QuestionDTO {
    private Long id;
    private String questionText;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id, String questionText) {
        this.id = id;
        this.questionText = questionText;
    }

    public QuestionDTO(Question question){
        this.id = question.getId();
        this.questionText = question.getQuestionText();
    }

    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }
}
