package com.grandp.data.entity.user;

public class UserHelper {

    public static final String REGEX_NAME = "\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+";
    public static final String REGEX_EMAIL = "/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$/";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,32}$";
    public static final String REGEX_PERSONAL_ID = "^\\d{8,12}$";

}