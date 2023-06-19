package com.grandp.data;

import com.grandp.data.entity.user.UserHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String name = "Georgi Petrov";
        Pattern pattern = Pattern.compile(UserHelper.REGEX_NAME);
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            System.out.println("Match found");
        } else {
            System.out.println("No match found");
        }

        String email = "georgi.petrov-05@tu-sofia.bg";
        pattern = Pattern.compile(UserHelper.REGEX_EMAIL);
//        matcher =


    }

}
