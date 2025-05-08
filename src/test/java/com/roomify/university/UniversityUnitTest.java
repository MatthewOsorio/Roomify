package com.roomify.university;

import com.roomify.university.UniversityDTO.UniversityDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UniversityUnitTest {

    @Test
    public void constructorWithId() {
        University university = new University(1L, "UNR", "Reno", "1664 N. Virginia St", 89557, "NV");

        assertEquals(1L, university.getId());
        assertEquals("UNR", university.getName());
        assertEquals("Reno", university.getCity());
        assertEquals("1664 N. Virginia St", university.getStreet());
        assertEquals(89557, university.getZipCode());
        assertEquals("NV", university.getState());
    }

    @Test
    public void ConstructorWithoutId() {
        University university = new University("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");

        assertNull(university.getId());
        assertEquals("UNR", university.getName());
        assertEquals("Reno", university.getCity());
        assertEquals("1664 N. Virginia St", university.getStreet());
        assertEquals(89557, university.getZipCode());
        assertEquals("NV", university.getState());
    }

    @Test
    public void testConstructorFromDTO() {
        UniversityDTO dto = new UniversityDTO("UNR", "Reno", "1664 N. Virginia St", 89557, "NV");
        University university = new University(dto);

        assertEquals("UNR", university.getName());
        assertEquals("Reno", university.getCity());
        assertEquals("1664 N. Virginia St", university.getStreet());
        assertEquals(89557, university.getZipCode());
        assertEquals("NV", university.getState());
    }

    @Test
    public void settersAndGetters() {
        University university = new University();

        university.setId(10L);
        university.setName("UNLV");
        university.setCity("Las Vegas");
        university.setStreet("4505 S. Maryland Pkwy");
        university.setZipCode(89154);
        university.setState("NV");

        assertEquals(10L, university.getId());
        assertEquals("UNLV", university.getName());
        assertEquals("Las Vegas", university.getCity());
        assertEquals("4505 S. Maryland Pkwy", university.getStreet());
        assertEquals(89154, university.getZipCode());
        assertEquals("NV", university.getState());
    }
}
