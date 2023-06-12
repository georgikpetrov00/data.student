package com.grandp.data.entity.enumerated;

import com.grandp.data.exception.notfound.runtime.SemesterNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Semester {
    FIRST("FIRST"),
    SECOND("SECOND"),
    THIRD("THIRD"),
    FOURTH("FOURTH"),
    FIFTH("FIFTH"),
    SIXTH("SIXTH"),
    SEVENTH("SEVENTH"),
    EIGHTH("EIGHTH");

    private final String value;
    private static final List<String> list = Arrays.stream(Semester.values()).map(Semester::name).collect(Collectors.toList());

    Semester(String value) {
        this.value = value;
    }

    public static Semester of(@NotNull String semester) {
        switch (semester.toUpperCase()) {
            case "FIRST": return FIRST;
            case "SECOND": return SECOND;
            case "THIRD": return THIRD;
            case "FOURTH": return FOURTH;
            case "FIFTH": return FIFTH;
            case "SIXTH": return SIXTH;
            case "SEVENTH": return SEVENTH;
            case "EIGHTH": return EIGHTH;
            default: throw new SemesterNotFoundException("Semester '" + semester + "' does not exist.");
        }
    }

    public String getValue() {
        return value;
    }

    public static List<String> getValues() {
        return new ArrayList<>(list);
    }
}