package com.andoverrobotics.inventory.web;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.json.JsonPersistence;
import com.andoverrobotics.inventory.security.GoogleAccountVerifier;
import com.andoverrobotics.inventory.web.cache.CacheEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class WebApplication {
    public static Foundation foundation;
    public static ConcurrentHashMap<String, CacheEntity> userCache;
    public static EncryptionManager encryptionManager;

    public static void main(String[] args) {
        try {
            JsonPersistence jsonPersistence = new JsonPersistence("data.json");
            GoogleAccountVerifier googleAccountVerifier = new GoogleAccountVerifier();
            foundation = new Foundation(jsonPersistence, googleAccountVerifier);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        userCache = new ConcurrentHashMap<>();
        encryptionManager = new EncryptionManager();


        SpringApplication.run(WebApplication.class, args);
    }

}
