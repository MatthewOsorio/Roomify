package com.roomify.matching;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.roomify.gpt.GptMatchingService;
import com.roomify.student.Student;
import com.roomify.student.StudentRepository;
import com.roomify.survery.Question;
import com.roomify.survery.QuestionRepository;

@Service
public class MatchingService {
    private final StudentRepository studentRepository;
    private final GptMatchingService gptMatchingService;
    private final QuestionRepository questionRepository;

    @Autowired
    public MatchingService(StudentRepository studentRepository, GptMatchingService gptMatchingService, QuestionRepository questionRepository) {
        this.studentRepository = studentRepository;
        this.gptMatchingService = gptMatchingService;
        this.questionRepository = questionRepository;
    }

    public List<Match> findMatches(Long studentId) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);

        if (existingStudent.isEmpty()) {
            throw new IllegalStateException("Student with ID " + studentId + " does not exist.");
        }

        
        List<Student> potentialMatches = studentRepository.findStudentsWithAnswersFetchedByUniversityIdAndSex(existingStudent.get().getUniversity().getId(), existingStudent.get().getSex());
        potentialMatches.removeIf(student -> student.getId().equals(studentId)); 

        List<Question> questions = questionRepository.findAll();
        JsonNode matches = gptMatchingService.generateStudentMatches(existingStudent.get(), potentialMatches, questions);

        List<Match> matchList = new ArrayList<>();
        for (JsonNode match : matches) {
            long matchedStudentId = match.get("student_id").asLong();
            double score = match.get("compatibility_score").asDouble();
            Optional<Student> matchedStudent = studentRepository.findById(matchedStudentId);
            matchList.add(new Match(matchedStudent.get(), score));
        }

        return matchList;
    }

}
