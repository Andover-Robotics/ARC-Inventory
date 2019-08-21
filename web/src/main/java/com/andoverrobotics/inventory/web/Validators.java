package com.andoverrobotics.inventory.web;

import java.util.regex.Pattern;

public class Validators {
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public static boolean isLoggedIn(String encryptedEmail) {
        String decryptedEmail = WebApplication.encryptionManager.decrypt(encryptedEmail);

        if(Validators.isValidEmail(decryptedEmail)) {
            // Off to a good start, valid email

            if(CacheManager.getIdentity(decryptedEmail) != null) {
                // Has an entity in the HashMap cache, they're logged in
                return true;
            }
        }

        return false;
    }
}
