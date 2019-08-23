package com.andoverrobotics.inventory.web.cache;

import com.andoverrobotics.inventory.web.WebApplication;

import java.util.TimerTask;

public class CacheClearTask extends TimerTask {
    private String email;

    public CacheClearTask(String email) {
        super();
        this.email = email;
    }

    @Override
    public void run() {
        CacheManager.removeNode(email);
        System.out.println(WebApplication.userCache.size());
    }
}
