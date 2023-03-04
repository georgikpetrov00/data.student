package com.grandp.data.program;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
//@Table(name = "programs")
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ElementCollection
    @CollectionTable(name = "program_subjects",
                     joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "subject_id")
    private List<Integer> subjectIds;

    public Program() {}

    @JsonCreator
    public Program(@JsonProperty("subjectIds") List<Integer> subjectIds) {
        this.subjectIds = subjectIds;
    }
    

    public long getId() {
    	return this.id;
    }
    
}