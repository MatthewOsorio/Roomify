    package com.roomify.survery;

    import java.util.List;
    import java.util.Optional;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import com.roomify.student.Student;
    import com.roomify.student.StudentRepository;
    import com.roomify.survery.SurveyDTO.SumbissionSurveryDTO;

    import jakarta.transaction.Transactional;

    @Service
    public class SurveyService {
        private final QuestionRepository questionRepository;
        private final AnswerRepository answerRepository;
        private final StudentRepository studentRepository;

        @Autowired
        public SurveyService(QuestionRepository questionRepository, AnswerRepository answerRepository, StudentRepository studentRepository) {
            this.questionRepository = questionRepository;
            this.answerRepository = answerRepository;
            this.studentRepository = studentRepository;
        }

        @Transactional
        public void submitSurvey(Long studentId, SumbissionSurveryDTO sumbissionSurveryDTO) {
            Optional<Student> existingStudent = studentRepository.findById(studentId);

            if (existingStudent.isEmpty()) {
                throw new IllegalStateException("Student with ID " + studentId + " does not exist.");
            }

            if (answerRepository.existsByStudentId(existingStudent.get().getId())) {
                throw new IllegalStateException("Student with ID " + studentId + " has already submitted the survey.");
            }

            List<Answer> answers = sumbissionSurveryDTO.getAnswers().stream().map(answerDTO -> {
                    Question question = questionRepository.findById(answerDTO.getQuestion().getId())
                        .orElseThrow(() -> new IllegalStateException("Question with ID " + answerDTO.getQuestion().getId() + " does not exist."));

                        Answer answer = new Answer(answerDTO.getAnswerText(), question, existingStudent.get());
                        return answer;
                    }).toList();
            
            answerRepository.saveAll(answers);
        }
    }