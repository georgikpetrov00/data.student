package com.grandp.data.entity.enumerated;

import com.grandp.data.exception.notfound.runtime.DegreeNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Degree {
    BACHELOR("БАКАЛАВЪР"),
    MASTER("МАГИСТЪР"),
    DOCTOR("ДОКТОРАНТУРА");

    public final String value;

    private static final List<String> list = Arrays.stream(Degree.values()).map(Degree::name).collect(Collectors.toList());


    Degree(String value) {
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
        throw new DegreeNotFoundException("Degree '" + value + "' does not exist.");
    }

    public static List<String> getValues() {
        return new ArrayList<>(list);
    }
}