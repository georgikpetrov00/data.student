package com.grandp.data.faculty;

public record FacultyDTO(String name, String abbreviation) {

    public static FacultyDTO of(Faculty faculty) {
        String name = faculty.getName();
        String abbreviation = faculty.getAbbreviation();
        return new FacultyDTO(name, abbreviation);
    }

}
