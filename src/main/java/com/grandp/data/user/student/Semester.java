package com.grandp.data.user.student;

import jakarta.validation.constraints.NotNull;

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

    private Semester(String value) {
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
            default: return null;
        }
    }

    public String getValue() {
        return value;
    }
}