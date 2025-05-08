package com.roomify.survey;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.PostgresTestContainer;
import com.roomify.student.Student;
import com.roomify.student.StudentRepository;
import com.roomify.survery.Question;
import com.roomify.survery.QuestionRepository;
import com.roomify.survery.SurveyDTO.AnswerDTO;
import com.roomify.survery.SurveyDTO.QuestionDTO;
import com.roomify.survery.SurveyDTO.SumbissionSurveryDTO;
import com.roomify.university.University;
import com.roomify.university.UniversityRepository;

import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SurveyControllerIntegrationTest extends PostgresTestContainer{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private QuestionRepository questionRepository;


    private List<Question> questions;
    private Student student;

    @BeforeEach
    void setup() {
        String uniqueName = "University of Nevada, Reno - " + UUID.randomUUID();
        University university = new University(uniqueName, "Reno", "1664 N. Virginia Street", 89557, "NV");
        universityRepository.save(university);
    
        student = new Student("Matthew", "Osorio", "matthewosorio@unr.edu", university, "password", "2002-12-21", 'M');
        studentRepository.save(student);
        String uuid = UUID.randomUUID().toString();

        questions = List.of(
            new Question("What does a 'clean' space mean to you, and what's your threshold before something feels messy or unlivable?" + uuid),
            new Question("When you're annoyed about something small a roommate does, how do you usually bring it up or do you let it slide?" + uuid),
            new Question("What does a typical weekday and weekend look like for you at home?" + uuid),
            new Question("What are your non-negotiables in a roommate situation? What's something small that ended up being a big deal for you in the past?" + uuid),
            new Question("Do you see a roommate as more of a co-habitant, a close friend, or something else entirely? Why?" + uuid),
            new Question("How do you feel about people staying over—romantic partners, friends from out of town, etc.? Any boundaries you like to set?" + uuid)
        );
        questionRepository.saveAll(questions);
    }

    @Test
    public void addSurvey_ValidInput() throws Exception {
        List<AnswerDTO> answers = List.of(
            new AnswerDTO("It means being very clean", new QuestionDTO(questions.get(0))),
            new AnswerDTO("I usually let it slide", new QuestionDTO(questions.get(1))),
            new AnswerDTO("I usually study and go to the gym", new QuestionDTO(questions.get(2))),
            new AnswerDTO("I don't like it when they leave the dishes dirty", new QuestionDTO(questions.get(3))),
            new AnswerDTO("I see them as a close friend", new QuestionDTO(questions.get(4))),
            new AnswerDTO("I don't mind them staying over", new QuestionDTO(questions.get(5)))
        );

        SumbissionSurveryDTO submissionSurveyDTO = new SumbissionSurveryDTO(answers);

        mockMvc.perform(post(String.format("/api/v1/student/%s/survey", student.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(submissionSurveyDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void addSurvey_StudentNotFound() throws Exception {
        List<AnswerDTO> answers = List.of(
            new AnswerDTO("It means being very clean", new QuestionDTO(questions.get(0))),
            new AnswerDTO("I usually let it slide", new QuestionDTO(questions.get(1))),
            new AnswerDTO("I usually study and go to the gym", new QuestionDTO(questions.get(2))),
            new AnswerDTO("I don't like it when they leave the dishes dirty", new QuestionDTO(questions.get(3))),
            new AnswerDTO("I see them as a close friend", new QuestionDTO(questions.get(4))),
            new AnswerDTO("I don't mind them staying over", new QuestionDTO(questions.get(5)))
        );

        SumbissionSurveryDTO submissionSurveyDTO = new SumbissionSurveryDTO(answers);

        mockMvc.perform(post("/api/v1/student/999/survey")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(submissionSurveyDTO)))
                .andExpect(status().isConflict());
    }

    @Test void addSurvey_InvalidQuestion() throws Exception {
        Question badQuestion = new Question("This question does not exist");
        badQuestion.setId(999L); 
        List<AnswerDTO> answers = List.of(
            new AnswerDTO("It means being very clean", new QuestionDTO(badQuestion)),
            new AnswerDTO("I usually let it slide", new QuestionDTO(questions.get(1))),
            new AnswerDTO("I usually study and go to the gym", new QuestionDTO(questions.get(2))),
            new AnswerDTO("I don't like it when they leave the dishes dirty", new QuestionDTO(questions.get(3))),
            new AnswerDTO("I see them as a close friend", new QuestionDTO(questions.get(4))),
            new AnswerDTO("I don't mind them staying over", new QuestionDTO(questions.get(5)))
        );

        SumbissionSurveryDTO submissionSurveyDTO = new SumbissionSurveryDTO(answers);

        mockMvc.perform(post(String.format("/api/v1/student/%s/survey", student.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(submissionSurveyDTO)))
                .andExpect(status().isConflict());
    }

}
