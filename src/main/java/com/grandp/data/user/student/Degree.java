package com.grandp.data.user.student;

public enum Degree {
    BACHELOR("Bachelor"),
    MASTER("Master"),
    DOCTOR("Doctor");

    private final String value;

    private Degree(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Degree of(String value) {
        for (Degree degree : values()) {
            if (degree.getValue().equalsIgnoreCase(value)) {
                return degree;
            }
        }
        throw new IllegalArgumentException("Invalid Degree value: " + value);
    }
}