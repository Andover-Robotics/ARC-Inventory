package com.andoverrobotics.inventory.web.login;

public class LoginPOSTBody {
    private String token;
    private String email;

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
