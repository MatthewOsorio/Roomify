package com.roomify.student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findStudentByEmailIgnoreCase(String email);

    @Query("""
    SELECT DISTINCT s 
    FROM Student s 
    JOIN FETCH s.answers a 
    WHERE s.university.id = :universityId AND s.sex = :sex
    """)
    List<Student> findStudentsWithAnswersFetchedByUniversityIdAndSex(@Param("universityId") Long universityId, @Param("sex") char sex);


}
