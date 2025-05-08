package com.roomify.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import com.roomify.student.Student;
import com.roomify.survery.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GptMatchingService {
    private final GptClient gptClient;

    public GptMatchingService(GptClient gptClient) {
        this.gptClient = gptClient;
    }

    public JsonNode generateStudentMatches(Student student, List<Student> potentialMatches, List<Question> questions) {
        return gptClient.generateMatches(student, potentialMatches, questions);
    }
}
