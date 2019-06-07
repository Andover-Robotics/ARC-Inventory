package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ConsumptionTest {

  private PartType part = new PartType(
      "Yellow Jacket Planetary Gear Motor",
      "5202-0002-0005",
      "goBILDA",
      "Motors",
      "Motor Box",
      "All",
      20,
      new URL("http://localhost"), new URL("http://localhost"),
      "1150", "5.2:1"
  );
  private Collection<PartType> inventory;

  public ConsumptionTest() throws MalformedURLException {
  }

  @Before
  public void setUp() throws Exception {
    inventory = Collections.singletonList(part);
  }

  @Test
  public void decrementsQuantityWhenAppliedAndPresent() {
    var consume = new Consumption("5202-0002-0005", 3);

    consume.apply(inventory);

    assertEquals(20 - 3, part.getQuantity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsExceptionWhenAppliedAndNotPresent() {
    var consume = new Consumption("0001-NON-EXISTENT", 1);

    consume.apply(inventory);
  }
}