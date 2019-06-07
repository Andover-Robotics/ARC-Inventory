package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class AdditionTest {
  private PartType newPart = new PartType(null, null, null, null,
      null, null, 0, null, null);
  private Collection<PartType> inventory = new LinkedList<>();

  @Before
  public void setUp() {
    inventory.clear();
  }

  @Test
  public void addsPartTypeToInventoryWhenApplied() {
    var add = new Addition(newPart);

    add.apply(inventory);

    assertTrue(inventory.contains(newPart));
    assertEquals(1, inventory.size());
  }
}