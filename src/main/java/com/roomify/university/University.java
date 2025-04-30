package com.roomify.university;

import java.util.HashSet;
import java.util.Set;

import com.roomify.student.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(
    uniqueConstraints =  @UniqueConstraint(columnNames = "name")
)
@Entity
public class University {
    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    private Long id;

    private String name;
    private String city;
    private String street;
    private Integer zipCode;
    private String state;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    public University() {
    }

    public University(Long id, String name, String city, String street, Integer zipCode, String state) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.state = state;
    }

    public University(String name, String city, String street, Integer zipCode, String state) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
