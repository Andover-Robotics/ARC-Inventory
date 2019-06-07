package com.andoverrobotics.inventory;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class PartTypeTest {
  @Test
  public void hasAllAttributes() throws NoSuchMethodException {
    String[] attributes = {
        "name",
        "partNumber",
        "brand",
        "category",
        "location",
        "team",
        "keywords",
        "quantity",
        "url",
        "imageUrl"
    };

    for (String attribute : attributes) {
      PartType.class.getMethod("get" + capitalized(attribute));
    }
  }

  @Test
  public void consumption() {
    PartType part = new PartType(null, null, null, null, null,
        null, 10, null, null);

    part.consume(4);

    assertEquals(10 - 4, part.getQuantity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void consumptionBeyondQuantity() {
    PartType part = new PartType(null, null, null, null, null,
        null, 10, null, null);

    part.consume(14);
  }

  @Test(expected = IllegalArgumentException.class)
  public void initializationWithNegativeQuantity() {
    new PartType(
        "Yellow Jacket Planetary Gear Motor",
        "5202-0002-0005",
        "goBILDA",
        "Motors",
        "Motor Box",
        "All",
        -1,
        null, null,
        "1150", "5.2:1"
    );
  }

  private String capitalized(String attribute) {
    return attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
  }
}