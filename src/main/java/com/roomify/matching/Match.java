package com.roomify.matching;

import com.roomify.student.Student;

public class Match {
    private Student student;
    private double score;

    public Match(Student student, double score) {
        this.student = student;
        this.score = score;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public double getScore() {
        return score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }
}
