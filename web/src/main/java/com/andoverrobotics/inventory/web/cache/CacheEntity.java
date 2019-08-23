package com.andoverrobotics.inventory.web.cache;

import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.web.GlobalConfig;
import com.andoverrobotics.inventory.web.WebApplication;

import java.util.Timer;
import java.util.TimerTask;

public class CacheEntity {
    private GoogleIdentity googleIdentity;
    private TimerTask clearTask;
    private Timer timer;
    private String email;

    public CacheEntity(GoogleIdentity googleIdentity, String email) {
        this.googleIdentity = googleIdentity;
        this.email = email;
        this.timer = new Timer();

        this.clearTask = new CacheClearTask(email);

        // Schedule the removal of the cache entity when the session expires
        this.timer.schedule(clearTask, GlobalConfig.LOGIN_SESSION_TIME_IN_SEC * 1000);
    }

    public GoogleIdentity getGoogleIdentity() {
        return googleIdentity;
    }

    // The user logged in again for some reason, restart the expiration timer
    public void refreshSession() {
        timer.cancel();
        timer.purge();

        // Restarting the task requires a new instance of timer and clearTask
        timer = new Timer();
        clearTask = new CacheClearTask(email);

        timer.schedule(clearTask, GlobalConfig.LOGIN_SESSION_TIME_IN_SEC * 1000);
    }
}
