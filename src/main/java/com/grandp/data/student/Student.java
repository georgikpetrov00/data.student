package com.grandp.data.student;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grandp.data.hasher.PasswordHasher;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Integer course;
	private Integer semester; 
	
	private Integer faculty;
	
	private String firstName;
	private String lastName;
	private String email;
	
	private String personalId;
	private String facultyNumber;
	private String password;
	
	private int currentSemesterProgram;
	private Set<Integer> program;

	public Student() {
	
	}

	
	@JsonCreator
    public Student(
                   @JsonProperty("firstName") String firstName,
                   @JsonProperty("lastName") String lastName,
                   @JsonProperty("email") String email,
                   @JsonProperty("personalId") String personalId,
                   @JsonProperty("facultyNumber") String facultyNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
		this.personalId = checkNumericField(personalId, StudentUtils.PERSONAL_ID);
		this.facultyNumber = checkField(facultyNumber, StudentUtils.FACULTY_NUMBER);
		this.password = PasswordHasher.hashPassword(facultyNumber); //first password is faculty number
    }

	private String checkField(String input, String fieldName) {
		assertNotNull(input, fieldName);
		
		return input;
	}
	
	private String checkNumericField(String personalId, String fieldName) {
		assertNotNull(personalId, fieldName);
		assertOnlyDigits(personalId, fieldName);
		
		return personalId;
	}
	
	private void assertNotNull(String input, String fieldName) {
		if (input == null) {
			throw new IllegalArgumentException(fieldName + " cannot be null.");
		}
	}
	
	private void assertOnlyDigits(String input, String fieldName) {
		if (!input.matches(StudentUtils.REGEXT_DIGIST_ONLY)) {
			throw new IllegalArgumentException(fieldName + " can contain only digits.");
		}
	}
	
	public long getId() {
		return this.id;
	}
}