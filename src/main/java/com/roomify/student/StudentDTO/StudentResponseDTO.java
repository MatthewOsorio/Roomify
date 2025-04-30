package com.roomify.student.StudentDTO;

import com.roomify.student.Student;
import com.roomify.university.UniversityDTO.UniversityDTO;

public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UniversityDTO university;
    private Integer age;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName    = student.getLastName();
        this.email = student.getEmail();
        this.university = new UniversityDTO(student.getUniversity());
        this.age = student.getAge();
    }

    public Long getId(){
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UniversityDTO getUniversity() {
        return university;
    }

    public Integer getAge() {
        return age;
    }
}
