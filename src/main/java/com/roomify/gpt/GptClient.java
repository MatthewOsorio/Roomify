package com.roomify.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.roomify.student.Student;
import com.roomify.survery.Question;

import java.util.List;

public interface GptClient {
    JsonNode generateMatches(Student currentStudent, List<Student> potentialMatches, List<Question> questions);
}
