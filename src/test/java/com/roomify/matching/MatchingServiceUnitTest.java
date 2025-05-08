package com.roomify.matching;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomify.gpt.GptMatchingService;
import com.roomify.student.Student;
import com.roomify.student.StudentRepository;
import com.roomify.survery.Question;
import com.roomify.survery.QuestionRepository;
import com.roomify.university.University;

@ExtendWith(MockitoExtension.class)
public class MatchingServiceUnitTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GptMatchingService gptMatchingService;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private MatchingService matchingService;   
    
    private Student student;;
    private Student match1;
    private Student match2;
    private List<Question> questions;
    private List<Student> potentialMatches;

    @BeforeEach
    void setup(){
        University university = new University("University of Nevada, Reno", "Reno", "1664 N. Virginia Street", 89557, "NV");

        student = new Student("Matthew", "Osorio", "matthewosorio@unr.edu", university, "password1", "2002-12-21", 'M');
        student.setId(1L);

        match1 = new Student("John", "Doe", "johndoe@unr.edu", university , "password2", "2001-01-01", 'M');
        match1.setId(2L);

        match2 = new Student("Mark", "Coleman", "markcoleman@unr.edu", university, "password3", "2000-02-02", 'M');
        match2.setId(3L);
        potentialMatches = List.of(match1, match2);

        questions = List.of(
            new Question("What does a 'clean' space mean to you, and what's your threshold before something feels messy or unlivable?"),
            new Question("When you're annoyed about something small a roommate does, how do you usually bring it up or do you let it slide?"),
            new Question("What does a typical weekday and weekend look like for you at home?"),
            new Question("What are your non-negotiables in a roommate situation? What's something small that ended up being a big deal for you in the past?"),
            new Question("Do you see a roommate as more of a co-habitant, a close friend, or something else entirely? Why?"),
            new Question("How do you feel about people staying over—romantic partners, friends from out of town, etc.? Any boundaries you like to set?")
        );
    }

    @Test
    void findMatches_returnValidMatches() throws Exception{
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentRepository.findStudentsWithAnswersFetchedByUniversityIdAndSex(student.getUniversity().getId(), student.getSex())).thenReturn(new ArrayList<>(List.of(student, match1, match2)));
        when(questionRepository.findAll()).thenReturn(questions);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mockGptResponse = objectMapper.readTree("""
            [
              { "student_id": 2, "compatibility_score": 0.87 },
              { "student_id": 3, "compatibility_score": 0.92 }
            ]
            """);

        when(gptMatchingService.generateStudentMatches(student, potentialMatches, questions)).thenReturn(mockGptResponse);
        when(studentRepository.findById(match1.getId())).thenReturn(Optional.of(match1));
        when(studentRepository.findById(match2.getId())).thenReturn(Optional.of(match2));
        
        matchingService.findMatches(student.getId());

        verify(studentRepository).findStudentsWithAnswersFetchedByUniversityIdAndSex(student.getUniversity().getId(), student.getSex());
        verify(questionRepository).findAll();
        verify(gptMatchingService).generateStudentMatches(student, potentialMatches, questions);
        verify(studentRepository, times(3)).findById(anyLong());
    }

    @Test
    void findMatches_studentNotFound(){
        when(studentRepository.findById(student.getId())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> matchingService.findMatches(student.getId())
        );

        
        assertEquals(String.format("Student with ID %s does not exist.", student.getId()), exception.getMessage());
        verify(gptMatchingService, never()).generateStudentMatches(student, potentialMatches, questions);
        verify(studentRepository, never()).findStudentsWithAnswersFetchedByUniversityIdAndSex(student.getUniversity().getId(), student.getSex());
        verify(questionRepository, never()).findAll();
        verify(studentRepository).findById(student.getId());        
    }
}
