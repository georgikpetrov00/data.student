package com.grandp.data.entity.faculty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    @Column(name = "abbreviation", unique = true)
    @NotNull
    private String abbreviation;

	public Faculty() {
		
	}
	
	@JsonCreator
    public Faculty(@JsonProperty("name") String name, @JsonProperty("abbreviation") String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }
	
    
    // Getters and setters
}