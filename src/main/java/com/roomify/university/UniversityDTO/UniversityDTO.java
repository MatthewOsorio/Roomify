package com.roomify.university.UniversityDTO;

import com.roomify.university.University;

public class UniversityDTO {

    private Long id;
    private String name;
    private String city;
    private String street;
    private Integer zipCode;
    private String state;

    public UniversityDTO() {}

    public UniversityDTO(String name, String city, String street, Integer zipCode, String state) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.state = state;
    }

    public UniversityDTO(University university) {
        this.id = university.getId();
        this.name = university.getName();
        this.city = university.getCity();
        this.street = university.getStreet();
        this.zipCode = university.getZipCode();
        this.state = university.getState();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public String getState() {
        return state;
    }
}
