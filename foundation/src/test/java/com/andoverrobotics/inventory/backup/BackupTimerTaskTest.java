package com.andoverrobotics.inventory.backup;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.PersistenceGateway;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BackupTimerTaskTest {
  private PersistenceGateway persistence = mock(PersistenceGateway.class);
  private ByteArrayOutputStream outputStream;

  private BackupTimerTask task;

  @Before
  public void setUp() {
    reset(persistence);
    outputStream = new ByteArrayOutputStream();
    task = new BackupTimerTask(persistence, () -> outputStream);
  }

  @Test
  public void readFromGateway() {
    task.run();
    verify(persistence).getCurrentState();
  }

  @Test
  public void writePartTypesFromGatewayToStream() {
    when(persistence.getCurrentState()).thenReturn(Stream.of(
        new PartType("NAME2413", "514236", "Tetrix", "TEST",
            "TESTER Box", "11115", 1, null, null, "KEYWORD")
    ));

    task.run();

    var output = outputStream.toString();
    for (var info : Arrays.asList("NAME2413", "5142", "Tetrix", "TEST", "Box", "11115", "KEYWORD")) {
      assertTrue("output does not contain " + info, output.contains(info));
    }
  }
}