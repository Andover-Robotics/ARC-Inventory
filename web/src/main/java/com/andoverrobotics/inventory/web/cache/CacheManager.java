package com.andoverrobotics.inventory.web.cache;

import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.web.Validators;
import com.andoverrobotics.inventory.web.WebApplication;

public class CacheManager {
    public static CacheEntity addNode(String email, GoogleIdentity identity) {
        if (Validators.isValidEmail(email) && identity != null)
            return WebApplication.userCache.put(email, new CacheEntity(identity, email));
        return null;
    }

    public static CacheEntity removeNode(String email) {
        if (Validators.isValidEmail(email)) {
            CacheEntity removed = WebApplication.userCache.remove(email);
            if(removed != null) {
                removed.cancelRemoval();
            }
            return removed;
        }
        return null;
    }

    public static CacheEntity getEntity(String email) {
        if (Validators.isValidEmail(email))
            return WebApplication.userCache.get(email);
        return null;
    }
}
