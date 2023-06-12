package com.grandp.data.entity.subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDTO {

    private String name;

    public SubjectDTO() {}

    public SubjectDTO(String name) {
        this.name = name;
    }

    public SubjectDTO(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Cannot construct SubjectDTO, because given subject is null");
        }

        this.name = subject.getName().getName();
    }
}
