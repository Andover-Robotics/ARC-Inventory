package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class DeletionTest {
  private Deletion deletion;
  private PartType part = new PartType(
      "Yellow Jacket Planetary Gear Motor",
      "5202-0002-0005",
      "goBILDA",
      "Motors",
      "Motor Box",
      "All",
      20,
      null, null,
      "1150", "5.2:1"
  );

  @Test
  public void removesPartWithGivenUuidWhenPresent() {
    var inventory = new ArrayList<PartType>();
    inventory.add(part);
    deletion = new Deletion(part.getUuid());

    deletion.apply(inventory);

    assertTrue(inventory.isEmpty());
  }

  @Test(expected = NoSuchElementException.class)
  public void throwsExceptionWhenNotPresent() {
    var inventory = new ArrayList<PartType>();
    deletion = new Deletion(UUID.randomUUID());

    deletion.apply(inventory);
  }
}