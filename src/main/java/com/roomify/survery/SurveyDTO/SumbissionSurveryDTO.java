package com.roomify.survery.SurveyDTO;

import java.util.List;

public class SumbissionSurveryDTO {
    private List<AnswerDTO> answers;

    public SumbissionSurveryDTO() {
    }

    public SumbissionSurveryDTO(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }
}
