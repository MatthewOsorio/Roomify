package com.roomify.matching;

import com.roomify.student.StudentDTO.StudentResponseDTO;

public class MatchDTO {
    private StudentResponseDTO student;
    private double score;

    public MatchDTO(Match match){
        this.student = new StudentResponseDTO(match.getStudent());
        this.score = match.getScore();
    }

    public StudentResponseDTO getStudent() {
        return student;
    }

    public double getScore() {
        return score;
    }
}
