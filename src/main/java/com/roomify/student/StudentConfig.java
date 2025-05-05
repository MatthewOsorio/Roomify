package com.roomify.student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.roomify.survery.Answer;
import com.roomify.survery.Question;
import com.roomify.survery.QuestionRepository;
import com.roomify.university.University;
import com.roomify.university.UniversityRepository;

@Configuration
public class StudentConfig {

    @Bean(name = "studentDataLoader")
    CommandLineRunner commandLineRunner(
        StudentRepository studentRepository,
        QuestionRepository questionRepository,
        UniversityRepository universityRepository
    ) {
        return args -> {

            List<Question> seededQuestions = List.of(
                new Question("What does a 'clean' space mean to you, and what's your threshold before something feels messy or unlivable?"),
                new Question("When you're annoyed about something small a roommate does, how do you usually bring it up or do you let it slide?"),
                new Question("What does a typical weekday and weekend look like for you at home?"),
                new Question("What are your non-negotiables in a roommate situation? What's something small that ended up being a big deal for you in the past?"),
                new Question("Do you see a roommate as more of a co-habitant, a close friend, or something else entirely? Why?"),
                new Question("How do you feel about people staying over—romantic partners, friends from out of town, etc.? Any boundaries you like to set?")
            );
            questionRepository.saveAll(seededQuestions);

            University unr = universityRepository.findByName("University of Nevada, Reno")
                .orElseGet(() -> universityRepository.save(
                    new University("University of Nevada, Reno", "Reno", "1664 N. Virginia St", 89557, "NV")
                ));

            University unlv = universityRepository.findByName("University of Nevada, Las Vegas")
                .orElseGet(() -> universityRepository.save(
                    new University("University of Nevada, Las Vegas", "Las Vegas", "4505 S. Maryland Pkwy", 89154, "NV")
                ));

            List<Question> questions = questionRepository.findAll();
            if (questions.size() < 6) {
                throw new IllegalStateException("Expected 6 questions to be preloaded in SurveyConfig.");
            }

            List<Student> students = new ArrayList<>(List.of(
                new Student("Alice", "Nguyen", "alice@unr.edu", unr, "pass123", "2000-04-10", 'F'),
                new Student("James", "Lee", "james@unr.edu", unr, "pass123", "1999-12-01", 'M'),
                new Student("Sophia", "Wright", "sophia@unlv.edu", unlv, "pass123", "2001-07-25", 'F'),
                new Student("Noah", "Garcia", "noah@unlv.edu", unlv, "pass123", "1998-08-15", 'M'),
                new Student("Olivia", "Martinez", "olivia@unr.edu", unr, "pass123", "2002-03-03", 'F'),
                new Student("Liam", "Kim", "liam@unlv.edu", unlv, "pass123", "2001-06-18", 'M'),
                new Student("Emma", "Lopez", "emma@unr.edu", unr, "pass123", "1999-11-11", 'F'),
                new Student("Benjamin", "Perez", "ben@unlv.edu", unlv, "pass123", "1997-09-30", 'M')
            ));

            String[] firstNames = {"Ethan", "Mia", "Logan", "Aria", "Aiden", "Chloe", "Lucas", "Grace", "Caleb", "Zoe", "Nathan", "Leah", "Henry", "Nora", "Daniel", "Ava", "Jack", "Ella", "Isaac", "Lily"};
            String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Anderson", "White", "Harris", "Martin", "Thompson", "Walker", "Young", "King", "Scott", "Green", "Baker", "Hall", "Allen", "Wright", "Lopez", "Hill"};

            Random rand = new Random();

            for (int i = 0; i < 30; i++) {
                String first = firstNames[i % firstNames.length];
                String last = lastNames[i % lastNames.length];
                String email = first.toLowerCase() + last.toLowerCase() + i + "@student.edu";
                University uni = (i % 2 == 0) ? unr : unlv;
                char sex = (i % 2 == 0) ? 'M' : 'F';

                LocalDate dob = LocalDate.of(1997 + rand.nextInt(7), 1 + rand.nextInt(12), 1 + rand.nextInt(28));
                Student student = new Student(first, last, email, uni, "pass123", dob.toString(), sex);
                students.add(student);
            }

            String[] responses = {
                "Clean means no clutter, dishes done, and floors swept. I get uncomfortable if trash piles up.",
                "I bring it up casually if it bothers me. I'd rather talk than stay annoyed.",
                "Weekdays are busy, I'm mostly out. Weekends I relax or hang with friends.",
                "No loud noise late at night. Once, loud music every night really got to me.",
                "Mostly just a co-habitant, but being friendly helps. I don't expect us to be close.",
                "Overnight guests are fine with notice. Just don't overdo it every week."
            };
            for (Student student : students) {
                for (int i = 0; i < questions.size(); i++) {
                    // Cycle through responses predictably or use modulo to keep within bounds
                    String text = responses[(i + rand.nextInt(responses.length)) % responses.length];
                    Answer answer = new Answer(text, questions.get(i), student);
                    student.addAnswer(answer);
                }
            }

            studentRepository.saveAll(students);
        };
    }
}
