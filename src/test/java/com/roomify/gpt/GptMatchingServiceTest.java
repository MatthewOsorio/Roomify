package com.roomify.gpt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.student.Student;
import com.roomify.survery.Answer;
import com.roomify.survery.Question;

public class GptMatchingServiceTest {

    private GptClient gptClient;
    private GptMatchingService gptMatchingService;

    @BeforeEach
    void setup() {
        gptClient = mock(GptClient.class);
        gptMatchingService = new GptMatchingService(gptClient);
    }

    @Test
    void generateStudentMatches_delegatesToGptClientAndReturnsJson() throws Exception {
        // Arrange test data
        Question question = new Question("Do you like to cook?");
        question.setId(1L);

        Student currentStudent = new Student();
        currentStudent.setId(1L);
        currentStudent.addAnswer(new Answer("Yes", question, currentStudent));

        Student potentialMatch = new Student();
        potentialMatch.setId(2L);
        potentialMatch.addAnswer(new Answer("Sometimes", question, potentialMatch));

        List<Student> matches = List.of(potentialMatch);
        List<Question> questions = List.of(question);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mockJson = objectMapper.readTree("""
            [
                { "student_id": 2, "compatibility_score": 8.2 }
            ]
        """);

        when(gptClient.generateMatches(currentStudent, matches, questions)).thenReturn(mockJson);

        // Act
        JsonNode result = gptMatchingService.generateStudentMatches(currentStudent, matches, questions);

        // Assert
        assertNotNull(result);
        assertTrue(result.isArray());
        assertEquals(2, result.get(0).get("student_id").asInt());
        assertEquals(8.2, result.get(0).get("compatibility_score").asDouble());

        verify(gptClient, times(1)).generateMatches(currentStudent, matches, questions);
    }
}
