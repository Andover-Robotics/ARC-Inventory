package com.andoverrobotics.inventory.web;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.json.JsonPersistence;
import com.andoverrobotics.inventory.security.GoogleAccountVerifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebApplication {
    public static Foundation foundation;

    public static void main(String[] args) {
        try {
            JsonPersistence jsonPersistence = new JsonPersistence("data.json");
            GoogleAccountVerifier googleAccountVerifier = new GoogleAccountVerifier();
            foundation = new Foundation(jsonPersistence, googleAccountVerifier);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        SpringApplication.run(WebApplication.class, args);
    }

}
