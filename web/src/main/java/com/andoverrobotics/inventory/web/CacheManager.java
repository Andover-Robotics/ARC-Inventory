package com.andoverrobotics.inventory.web;

import com.andoverrobotics.inventory.security.GoogleIdentity;

public class CacheManager {
    public static GoogleIdentity addNode(String email, GoogleIdentity identity) {
        if (email != null && email.length() > 0 && identity != null)
            return WebApplication.userCache.put(email, identity);
        return null;
    }

    public static GoogleIdentity removeNode(String email) {
        if (email != null && email.length() > 0)
            return WebApplication.userCache.remove(email);
        return null;
    }

    public static GoogleIdentity getIdentity(String email) {
        if (email != null && email.length() > 0)
            return WebApplication.userCache.get(email);
        return null;
    }
}
