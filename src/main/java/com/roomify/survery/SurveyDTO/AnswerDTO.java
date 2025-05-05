package com.roomify.survery.SurveyDTO;

import com.roomify.survery.Answer;

public class AnswerDTO {
    private String answerText;
    private QuestionDTO question;

    public AnswerDTO() {
    }

    public AnswerDTO(String answerText, QuestionDTO question) {
        this.answerText = answerText;
        this.question = question;
    }
    
    public AnswerDTO(Answer answer){
        this.answerText = answer.getAnswerText();
        this.question = new QuestionDTO(answer.getQuestion());
    }

    public String getAnswerText() {
        return answerText;
    }

    public QuestionDTO getQuestion() {
        return question;
    }   
}
