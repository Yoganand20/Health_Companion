package com.project.healthcompanion;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    int minLength = 8, maxLength = 20;
    private static final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);

    private static final String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final Pattern emailPattern = Pattern.compile(emailRegex);

    public Validate() {
    }

    public static boolean ValidateField(EditText editText) {
        if (editText.length() == 0) {
            editText.setError("This field is required.");
            return false;
        }
        return true;
    }

    public static boolean ValidatePattern(EditText editText, Pattern regex) {
        Matcher matcher = regex.matcher(editText.getText().toString());
        if (!matcher.find()) {
            editText.setError("Invalid Input.");
            return false;
        }
        return true;
    }

    public static boolean ValidateLength(EditText editText, int minLength, int maxLength) {
        if (editText.length() < minLength) {
            editText.setError("Input too small.");
            return false;
        }
        if (editText.length() > maxLength) {
            editText.setError("Input too large.");
            return false;
        }
        return true;
    }

    public static int ValidateAll(EditText editText, Pattern regex, int minLength, int maxLength) {
        if (!ValidateField(editText)) {
            return 1;
        }
        if (!ValidateLength(editText, minLength, maxLength)) {
            return 2;
        }

        if (!ValidatePattern(editText, regex)) {
            return 3;
        }
        return 0;
    }

    public static int ValidateAll(EditText editText, Pattern regex) {
        if (!ValidateField(editText)) {
            return 1;
        }

        if (!ValidatePattern(editText, regex)) {
            return 3;
        }
        return 0;
    }


    public static boolean ValidateEmail(EditText email) {
        switch (Validate.ValidateAll(email, emailPattern)) {
            case 0:
                return true;
            case 1:
                email.setError("Email address not entered.");
                return false;
            case 3:
                email.setError("Invalid Email address.");
                return false;
            default:
                email.setError("Invalid");
                return false;
        }
    }

    public static boolean CheckPassword(EditText password) {
        if (Validate.ValidateField(password)) {
            return true;
        } else {
            password.setError("Password not entered.");
            return false;
        }
    }

    public static boolean ValidatePassword(EditText password, EditText c_password) {
        switch (ValidateAll(password, passwordPattern)) {
            case 0:
                if (password.getText().toString().contentEquals(c_password.getText().toString()))
                    return true;
                else
                    return false;
            case 1:
                password.setError("Password not entered.");
                return false;
            case 3:
                password.setError("Invalid password.Password should contain 1 lower case character, 1 upper case character, 1 special character and 1 number.");
                return false;
            default:
                password.setError("Invalid");
                return false;
        }
    }
}
