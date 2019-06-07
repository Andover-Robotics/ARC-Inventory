package com.andoverrobotics.inventory.backup;

import com.andoverrobotics.inventory.PersistenceGateway;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.TimerTask;
import java.util.function.Supplier;

public class BackupTimerTask extends TimerTask {
  private final PersistenceGateway persistence;
  private final Supplier<OutputStream> outputStream;

  public BackupTimerTask(PersistenceGateway persistence, Supplier<OutputStream> outputStream) {
    this.persistence = persistence;
    this.outputStream = outputStream;
  }

  @Override
  public void run() {
    PrintStream output = new PrintStream(outputStream.get());

    output.println(new Gson().toJson(
        persistence.getCurrentState().toArray()));

    output.close();
  }
}
