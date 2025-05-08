package com.roomify.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.*;
import com.roomify.student.Student;
import com.roomify.survery.Question;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class OpenAIGptClient implements GptClient {

    private final OpenAIClient client;

    public OpenAIGptClient() {
        this(OpenAIOkHttpClient.fromEnv());
    }

    public OpenAIGptClient(OpenAIClient client) {
        this.client = client;
    }

    @Override
    public JsonNode generateMatches(Student currentStudent, List<Student> potentialMatches, List<Question> questions) {
        StringBuilder questionString = new StringBuilder();
        for (Question question : questions) {
            questionString.append(question.toString()).append("\n");
        }

        StringBuilder primaryStudentAnswers = new StringBuilder();
        currentStudent.getAnswers().stream()
            .sorted(Comparator.comparing(a -> a.getQuestion().getId()))
            .forEach(answer -> primaryStudentAnswers.append(answer.getQuestion().getId()).append(": ").append(answer.getAnswerText()).append("\n"));

        StringBuilder potentialMatchesAnswers = new StringBuilder();
        for (Student student : potentialMatches) {
            potentialMatchesAnswers.append("Student ID: ").append(student.getId()).append("\n");
            student.getAnswers().stream()
                .sorted(Comparator.comparing(a -> a.getQuestion().getId()))
                .forEach(answer -> potentialMatchesAnswers.append(answer.getQuestion().getId()).append(": ").append(answer.getAnswerText()).append("\n"));
            potentialMatchesAnswers.append("\n");
        }

        String prompt = String.format("""
            These are questions from a survey that we are making students fill out to determine if they are compatible as roomates:
            %s

            Here are the answers from the primary student:
            %s

            Here are the answers from the potential matches:
            %s

            Based on the primary student's answers and the potential matches' answers, please provide a list of the top 5 potential matches that are most compatible with the primary student.
            Return the list in the following JSON format:
            {
                "matches": [
                    {
                        "student_id": 1,
                        "compatibility_score": 9.5
                    }
                ]
            }
            """, questionString, primaryStudentAnswers, potentialMatchesAnswers);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage(prompt)
            .model(ChatModel.CHATGPT_4O_LATEST)
            .build();

        ChatCompletion completion = client.chat().completions().create(params);
        Optional<String> messageContent = completion.choices().get(0).message().content();

        if (messageContent.isEmpty()) {
            throw new IllegalStateException("No content returned from GPT");
        }

        try {
            String cleaned = messageContent.get().replaceAll("^```json", "").replaceAll("```$", "").trim();
            return new ObjectMapper().readTree(cleaned).get("matches");
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse GPT response", e);
        }
    }
}
