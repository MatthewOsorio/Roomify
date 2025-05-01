package com.roomify.university;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long>{
    public Optional<University> findByNameIgnoreCase(String name);
}
