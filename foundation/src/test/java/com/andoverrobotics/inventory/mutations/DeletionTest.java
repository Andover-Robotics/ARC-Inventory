package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

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
  public void removesPartWithGivenNumberWhenPresent() {
    var inventory = new ArrayList<PartType>();
    inventory.add(part);
    deletion = new Deletion(part.getPartNumber());

    deletion.apply(inventory);

    assertTrue(inventory.isEmpty());
  }

  @Test(expected = NoSuchElementException.class)
  public void throwsExceptionWhenNotPresent() {
    var inventory = new ArrayList<PartType>();
    deletion = new Deletion("NONEXISTENT-PART");

    deletion.apply(inventory);
  }
}