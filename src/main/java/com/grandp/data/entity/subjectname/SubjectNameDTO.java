package com.grandp.data.entity.subjectname;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubjectNameDTO {

    private String name;

    public SubjectNameDTO(SubjectName subjectName) {
        this.name = subjectName.getName();
    }
}
