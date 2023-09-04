package com.grandp.data.entity.user;

public class UserHelper {

    public static final String REGEX_NAME = "^[A-Za-zА-Яа-яЀ-ӿ-'\s]*$";
    public static final String EMAIL_CONSTRAINT = "Invalid email. Email should look like 'email@example.com'";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,32}$";
    public static final String REGEX_PERSONAL_ID = "^\\d{8,12}$";

    public static final String NAME_CONSTRAINT = "name must start with capital letter containing only letters and/or special characters - comma, dot, hyphen, space or apostrophe.";
    public static final String INVALID_FNAME_MSG = "First " + NAME_CONSTRAINT;
    public static final String INVALID_LNAME_MSG = "Last" + NAME_CONSTRAINT;
    public static final String INVALID_PID_MSG = "Personal ID must contain only digits and must have length 8-12.";

}
