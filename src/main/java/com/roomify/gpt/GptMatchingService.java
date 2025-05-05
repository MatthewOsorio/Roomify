package com.roomify.gpt;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.roomify.student.Student;
import com.roomify.survery.Question;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;

@Service
public class GptMatchingService {
    OpenAIClient client;

    public GptMatchingService() {
        client = OpenAIOkHttpClient.fromEnv();
    }

    public JsonNode generateStudentMatches(Student currentStudent, List<Student> potentialMatches, List<Question> questions) {
        StringBuilder questionString = new StringBuilder();
        for (Question question : questions) {
            questionString.append(question.toString()).append("\n");
        }

        StringBuilder primaryStudentAnswers = new StringBuilder();
        currentStudent.getAnswers().stream()
            .sorted(Comparator.comparing(a -> a.getQuestion().getId()))
            .forEach(answer ->
                primaryStudentAnswers.append(answer.getQuestion().getId())
                    .append(": ")
                    .append(answer.getAnswerText())
                    .append("\n")
            );

        StringBuilder potentialMatchesAnswers = new StringBuilder();
        for (Student student : potentialMatches) {
            potentialMatchesAnswers.append("Student ID: ").append(student.getId()).append("\n");
            student.getAnswers().stream()
                .sorted(Comparator.comparing(a -> a.getQuestion().getId()))
                .forEach(answer ->
                    potentialMatchesAnswers.append(answer.getQuestion().getId())
                        .append(": ")
                        .append(answer.getAnswerText())
                        .append("\n")
                );
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
                    },
                    {
                        "student_id": 2,
                        "compatibility_score": 4.3
                    }
                ]
            }
            """, questionString, primaryStudentAnswers, potentialMatchesAnswers);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage(prompt)
            .model(ChatModel.CHATGPT_4O_LATEST)
            .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);
        Optional<String> messageContent = chatCompletion.choices().get(0).message().content();

        if (!messageContent.isPresent()) {
            throw new IllegalStateException("No message content returned from GPT-4.");
        } 

        try{
            String gptResponse  = messageContent.get().replaceAll("^```json", "").replaceAll("```$", "").trim();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(gptResponse);
            return root.get("matches");
        }catch(JsonProcessingException err){
            throw new IllegalStateException("Failed to parse GPT response", err);
        }
    }
}
