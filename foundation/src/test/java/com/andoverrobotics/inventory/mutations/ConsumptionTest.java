package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

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
  public void setUp() {
    inventory = Collections.singletonList(part);
  }

  @Test
  public void decrementsQuantityWhenAppliedAndPresent() {
    var consume = new Consumption(part.getUuid(), 3);

    consume.apply(inventory);

    assertEquals(20 - 3, part.getQuantity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void throwsExceptionWhenAppliedAndNotPresent() {
    var consume = new Consumption(UUID.randomUUID(), 1);

    consume.apply(inventory);
  }
}