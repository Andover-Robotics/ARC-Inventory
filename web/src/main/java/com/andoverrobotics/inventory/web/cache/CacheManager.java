package com.andoverrobotics.inventory.web.cache;

import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.web.WebApplication;

public class CacheManager {
    public static CacheEntity addNode(String email, GoogleIdentity identity) {
        if (email != null && email.length() > 0 && identity != null)
            return WebApplication.userCache.put(email, new CacheEntity(identity, email));
        return null;
    }

    public static CacheEntity removeNode(String email) {
        if (email != null && email.length() > 0)
            return WebApplication.userCache.remove(email);
        return null;
    }

    public static CacheEntity getEntity(String email) {
        if (email != null && email.length() > 0)
            return WebApplication.userCache.get(email);
        return null;
    }
}
