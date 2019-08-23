package com.andoverrobotics.inventory.web;

import com.andoverrobotics.inventory.Foundation;
import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.json.JsonPersistence;
import com.andoverrobotics.inventory.mutations.Addition;
import com.andoverrobotics.inventory.security.GoogleAccountVerifier;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.web.cache.CacheEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class WebApplication {
    public static Foundation foundation;
    public static ConcurrentHashMap<String, CacheEntity> userCache;
    public static EncryptionManager encryptionManager;

    public static void main(String[] args) throws MalformedURLException {
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

        // Add test data
        for (int i = 0; i < 101; i++) {
            PartType partToAdd = new PartType("48mm full channel", "1120-0002-0072", "AndyMark", "Tools", "MR Electronics Box", "5273", 42, new URL("https://www.gobilda.com/1112-series-low-side-l-channel-1-hole-48mm-length/"), new URL("https://cdn11.bigcommerce.com/s-eem7ijc77k/images/stencil/800x800/products/795/20015/1112-0001-0048__85676.1534280262.jpg?c=2"), "Good objects", "Great Objects", "The best objects");
            foundation.change(new GoogleIdentity("007", "james@MI6.net", "Bond, James Bond", "https://www.etonline.com/sites/default/files/styles/max_970x546/public/images/2018-05/gettyimages-494656120.jpg?itok=b0e7xle6&h=952c1ea1", "andoverrobotics.com"), new Addition(partToAdd));
        }

        SpringApplication.run(WebApplication.class, args);
    }

}
