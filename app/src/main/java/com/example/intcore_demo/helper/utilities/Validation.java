package com.example.intcore_demo.helper.utilities;


import java.util.regex.Pattern;

public class Validation {
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isVaildPhoneNumber(String phone) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        return p.matcher(phone).matches();
    }
}
