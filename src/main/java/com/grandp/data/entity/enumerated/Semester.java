package com.grandp.data.entity.enumerated;

import com.grandp.data.exception.notfound.runtime.SemesterNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Semester {
    NONE("ПРАЗНО", 0),
    FIRST("ПЪРВИ", 1),
    SECOND("ВТОРИ", 2),
    THIRD("ТРЕТИ", 3),
    FOURTH("ЧЕТВЪРТИ", 4),
    FIFTH("ПЕТИ", 5),
    SIXTH("ШЕСТИ", 6),
    SEVENTH("СЕДМИ", 7),
    EIGHTH("ОСМИ", 8);

    private final String value;
    private static final List<String> list = Arrays.stream(Semester.values()).map(Semester::name).collect(Collectors.toList());
    private final int intValue;
    private static final List<Integer> intList = Arrays.stream(Semester.values()).map(Semester::getIntValue).collect(Collectors.toList());

    Semester(String value, int intValue) {
        this.value = value;
        this.intValue = intValue;
    }

    public static Semester of(@NotNull String semester) throws SemesterNotFoundException {
        return switch (semester.toUpperCase()) {
            case "ПРАЗНО" -> NONE;
            case "ПЪРВИ" -> FIRST;
            case "ВТОРИ" -> SECOND;
            case "ТРЕТИ" -> THIRD;
            case "ЧЕТВЪРТИ" -> FOURTH;
            case "ПЕТИ" -> FIFTH;
            case "ШЕСТИ" -> SIXTH;
            case "СЕДМИ" -> SEVENTH;
            case "ОСМИ" -> EIGHTH;
            default -> throw new SemesterNotFoundException("Semester '" + semester + "' does not exist.");
        };
    }

    public static Semester of(@NotNull int semester) throws SemesterNotFoundException {
        return switch (semester) {
            case 0 -> NONE;
            case 1 -> FIRST;
            case 2 -> SECOND;
            case 3 -> THIRD;
            case 4 -> FOURTH;
            case 5 -> FIFTH;
            case 6 -> SIXTH;
            case 7 -> SEVENTH;
            case 8 -> EIGHTH;
            default -> throw new SemesterNotFoundException("Semester '" + semester + "' does not exist.");
        };
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