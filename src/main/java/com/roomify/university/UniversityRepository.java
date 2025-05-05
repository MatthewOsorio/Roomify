package com.roomify.university;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long>{
    public Optional<University> findByNameIgnoreCase(String name);
    public Optional<University> findByName(String name);
}
