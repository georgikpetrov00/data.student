package com.grandp.data.entity.enumerated;

import com.grandp.data.exception.notfound.runtime.SemesterNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Semester {
    NONE("NONE", 0),
    FIRST("FIRST", 1),
    SECOND("SECOND", 2),
    THIRD("THIRD", 3),
    FOURTH("FOURTH", 4),
    FIFTH("FIFTH", 5),
    SIXTH("SIXTH", 6),
    SEVENTH("SEVENTH", 7),
    EIGHTH("EIGHTH", 8);

    private final String value;
    private static final List<String> list = Arrays.stream(Semester.values()).map(Semester::name).collect(Collectors.toList());
    private final int intValue;
    private static final List<Integer> intList = Arrays.stream(Semester.values()).map(Semester::getIntValue).collect(Collectors.toList());

    Semester(String value, int intValue) {
        this.value = value;
        this.intValue = intValue;
    }

    public static Semester of(@NotNull String semester) throws SemesterNotFoundException {
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

    public static Semester of(@NotNull int semester) throws SemesterNotFoundException {
        switch (semester) {
            case 1: return FIRST;
            case 2: return SECOND;
            case 3: return THIRD;
            case 4: return FOURTH;
            case 5: return FIFTH;
            case 6: return SIXTH;
            case 7: return SEVENTH;
            case 8: return EIGHTH;
            default: throw new SemesterNotFoundException("Semester '" + semester + "' does not exist.");
        }
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() {
        return intValue;
    }

    public static List<String> getValues() {
        return new ArrayList<>(list);
    }
}