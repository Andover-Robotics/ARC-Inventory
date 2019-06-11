package com.andoverrobotics.inventory.query;

import com.andoverrobotics.inventory.PartType;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchQueryTest {
  private PartType yellowJacket = new PartType(
      "Yellow Jacket Planetary Gear Motor",
      "5202-0002-0005",
      "goBILDA",
      "Motors",
      "Motor Box",
      "All",
      2,
      new URL("http://localhost"), new URL("http://localhost"),
      "1150", "5.2:1"
  ),
  buttonHeadScrew = new PartType(
      "Button Head Screw 10mm",
      "2802-0004-0010",
      "goBILDA",
      "Hardware",
      "4410 Husky",
      "All",
      50,
      new URL("http://localhost"), new URL("http://localhost"),
      "screw", "bolt"
  );

  public SearchQueryTest() throws MalformedURLException {
  }

  @Test
  public void searchByName() {
    var testee = new SearchQuery("yellow jacket");

    assertTrue(testee.matches(yellowJacket));
    assertFalse(testee.matches(buttonHeadScrew));

    testee = new SearchQuery("head");

    assertTrue(testee.matches(buttonHeadScrew));
    assertFalse(testee.matches(yellowJacket));
  }

  @Test
  public void searchByPartNumber() {
    var testee = new SearchQuery("5202-0002");

    assertTrue(testee.matches(yellowJacket));
    assertFalse(testee.matches(buttonHeadScrew));

    testee = new SearchQuery("0004-0");

    assertFalse(testee.matches(yellowJacket));
    assertTrue(testee.matches(buttonHeadScrew));
  }

  @Test
  public void searchByBrand() {
    var testee = new SearchQuery("goBILDA");

    assertTrue(testee.matches(yellowJacket));
    assertTrue(testee.matches(buttonHeadScrew));
  }

  @Test
  public void searchByCategory() {
    var testee = new SearchQuery("motors");

    assertTrue(testee.matches(yellowJacket));
    assertFalse(testee.matches(buttonHeadScrew));
  }

  @Test
  public void searchByLocation() {
    var testee = new SearchQuery("4410 husky");

    assertFalse(testee.matches(yellowJacket));
    assertTrue(testee.matches(buttonHeadScrew));
  }

  @Test
  public void searchByKeywords() {
    var testee = new SearchQuery("5.2:1");

    assertTrue(testee.matches(yellowJacket));
    assertFalse(testee.matches(buttonHeadScrew));
  }
}