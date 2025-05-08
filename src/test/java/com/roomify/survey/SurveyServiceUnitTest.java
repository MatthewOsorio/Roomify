package com.roomify.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.roomify.student.Student;
import com.roomify.student.StudentRepository;
import com.roomify.survery.AnswerRepository;
import com.roomify.survery.Question;
import com.roomify.survery.QuestionRepository;
import com.roomify.survery.SurveyService;
import com.roomify.survery.SurveyDTO.AnswerDTO;
import com.roomify.survery.SurveyDTO.QuestionDTO;
import com.roomify.survery.SurveyDTO.SumbissionSurveryDTO;
import com.roomify.university.University;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceUnitTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private SurveyService surveyService;

    private SumbissionSurveryDTO sumbissionSurveryDTO;
    private Student student;

    private Question question1 = new Question("What does a 'clean' space mean to you, and what's your threshold before something feels messy or unlivable?");
    private Question question2 = new Question("When you're annoyed about something small a roommate does, how do you usually bring it up or do you let it slide?");
    private Question question3 = new Question("What does a typical weekday and weekend look like for you at home?");
    private Question question4 = new Question("What are your non-negotiables in a roommate situation? What's something small that ended up being a big deal for you in the past?");
    private Question question5 = new Question("Do you see a roommate as more of a co-habitant, a close friend, or something else entirely? Why?");
    private Question question6 = new Question("How do you feel about people staying over—romantic partners, friends from out of town, etc.? Any boundaries you like to set?");

    @BeforeEach
    void setup() {
        QuestionDTO questionDTO1 = new QuestionDTO(question1);
        QuestionDTO questionDTO2 = new QuestionDTO(question2);
        QuestionDTO questionDTO3 = new QuestionDTO(question3);
        QuestionDTO questionDTO4 = new QuestionDTO(question4);
        QuestionDTO questionDTO5 = new QuestionDTO(question5);
        QuestionDTO questionDTO6 = new QuestionDTO(question6);

        AnswerDTO answer1 = new AnswerDTO("A clean space means everything is in its place and there is no clutter.", questionDTO1);
        AnswerDTO answer2 = new AnswerDTO("I usually bring it up in a calm manner, but only if it happens repeatedly.", questionDTO2);
        AnswerDTO answer3 = new AnswerDTO("Weekdays are quiet and structured, weekends are more relaxed with occasional gatherings.", questionDTO3);
        AnswerDTO answer4 = new AnswerDTO("Non-negotiables include respecting personal space and cleanliness. A small issue was loud music late at night.", questionDTO4);
        AnswerDTO answer5 = new AnswerDTO("I see a roommate as a co-habitant, but a friendly relationship is a bonus.", questionDTO5);
        AnswerDTO answer6 = new AnswerDTO("I'm okay with people staying over occasionally, but I prefer to discuss it beforehand.", questionDTO6);

        List<AnswerDTO> answers = List.of(answer1, answer2, answer3, answer4, answer5, answer6);

        sumbissionSurveryDTO = new SumbissionSurveryDTO(answers);

        University university = new University("University of Nevada, Reno", "Reno", "1664 N. Virginia Street", 89557, "NV");
        student = new Student("Matthew", "Osorio", "matthewosorio@unr.edu", university, "password1", "2002-12-21", 'M');
    }

    @Test
    void submitSurvey_ValidInput() {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(answerRepository.existsByStudentId(student.getId())).thenReturn(false);
        when(questionRepository.findById(question1.getId())).thenReturn(Optional.of(question1));
        when(questionRepository.findById(question2.getId())).thenReturn(Optional.of(question2));
        when(questionRepository.findById(question3.getId())).thenReturn(Optional.of(question3));
        when(questionRepository.findById(question4.getId())).thenReturn(Optional.of(question4));
        when(questionRepository.findById(question5.getId())).thenReturn(Optional.of(question5));
        when(questionRepository.findById(question6.getId())).thenReturn(Optional.of(question6));

        surveyService.submitSurvey(student.getId(), sumbissionSurveryDTO);
        verify(answerRepository).saveAll(anyList());
    }

    @Test
    void submitSurvey_StudentNotFound() {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.empty());

        IllegalStateException expection = assertThrows(
            IllegalStateException.class,
            () -> surveyService.submitSurvey(student.getId(), sumbissionSurveryDTO)
        );

        assertEquals(String.format("Student with ID %s does not exist.", student.getId()), expection.getMessage());
        verify(answerRepository, never()).saveAll(anyList());
        verify(questionRepository, never()).findById(anyLong());
    }

    @Test
    void submitSurvey_StudentAlreadySubmitted() {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(answerRepository.existsByStudentId(student.getId())).thenReturn(true);

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> surveyService.submitSurvey(student.getId(), sumbissionSurveryDTO)
        );

        assertEquals(
            String.format("Student with ID %s has already submitted the survey.", student.getId()),
            exception.getMessage()
        );

        verify(answerRepository, never()).saveAll(anyList());
        verify(questionRepository, never()).findById(anyLong());
    }
       
    @Test
    void sumbitSurvey_QuestionNotFound() {
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(answerRepository.existsByStudentId(student.getId())).thenReturn(false);
        when(questionRepository.findById(question1.getId())).thenReturn(Optional.of(question1));
        when(questionRepository.findById(question2.getId())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> surveyService.submitSurvey(student.getId(), sumbissionSurveryDTO)
        );

        assertEquals(String.format("Question with ID %s does not exist.", question2.getId()), exception.getMessage());
        verify(answerRepository, never()).saveAll(anyList());
    }
}
