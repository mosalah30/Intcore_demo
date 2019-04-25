package com.example.intcore_demo.helper.utilities;


import android.util.Patterns;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidEmailAddress(String target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isValidPhoneNumber(String phone) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        return p.matcher(phone).matches();
    }

}
