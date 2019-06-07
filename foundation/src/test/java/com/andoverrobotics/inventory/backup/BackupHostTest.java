package com.andoverrobotics.inventory.backup;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.Mockito.*;

public class BackupHostTest {
  private TimerTask timerTask = mock(TimerTask.class);
  private Timer timer = mock(Timer.class);
  private BackupHost host;

  @Before
  public void setUp() {
    reset(timer, timerTask);
    host = new BackupHost(timer, timerTask);
  }

  @Test
  public void scheduleTimerWithDefaultInterval() {
    host.scheduleInterval();

    verify(timer).schedule(timerTask, 0, 1000 * 60 * 60 * 24 * 14);
  }

  @Test
  public void stopRecurrence() {
    host.scheduleInterval();

    host.cancelInterval();

    verify(timer).cancel();
  }

  @Test
  public void internalTimerExecution() throws InterruptedException {
    var runnable = mock(Runnable.class);
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        runnable.run();
      }
    };
    var host = new BackupHost(task);

    host.scheduleInterval(50);

    Thread.sleep(60);
    verify(runnable, times(2)).run();

  }
}