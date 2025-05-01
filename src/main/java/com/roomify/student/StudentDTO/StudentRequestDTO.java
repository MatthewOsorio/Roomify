package com.roomify.student.StudentDTO;

import com.roomify.university.UniversityDTO.UniversityDTO;

public class StudentRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private UniversityDTO university;
    private String password;
    private String dob;
    private char sex;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String firstName, String lastName, String email, UniversityDTO university, String password, String dob, char sex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.university = university;
        this.password = password;
        this.dob = dob;
        this.sex = sex;
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

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public char getSex() {
        return sex;
    }
}
