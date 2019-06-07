package com.andoverrobotics.inventory.backup;

import java.util.Timer;
import java.util.TimerTask;

public class BackupHost {
  // 2 weeks
  private static final long DEFAULT_INTERVAL_MS = 2 * 7 * 24 * 60 * 60 * 1000;

  private final Timer timer;
  private final TimerTask timerTask;

  public BackupHost(Timer timer, TimerTask timerTask) {
    this.timer = timer;
    this.timerTask = timerTask;
  }

  public BackupHost(TimerTask timerTask) {
    this(new Timer("BackupHost"), timerTask);
  }

  public void scheduleInterval() {
    scheduleInterval(DEFAULT_INTERVAL_MS);
  }

  public void scheduleInterval(long intervalMs) {
    timer.schedule(timerTask, 0, intervalMs);
  }

  public void cancelInterval() {
    timer.cancel();
  }
}
