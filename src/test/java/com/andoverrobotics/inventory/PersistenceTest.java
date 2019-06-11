package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.json.JsonPersistence;
import com.andoverrobotics.inventory.mutations.Addition;
import com.andoverrobotics.inventory.security.Identity;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class PersistenceTest {
  private PersistenceGateway gateway = new JsonPersistence("persistence.json");
  @Mock
  private Identity changer = null;

  private final PartType yellowJacket = new PartType(
      "Yellow Jacket Planetary Gear Motor",
      "5202-0002-0005",
      "goBILDA",
      "Motors",
      "Motor Box",
      "All",
      2,
      null, null,
      "1150", "5.2:1"
  ),
  tetrixScrew = new PartType(
      "Button Head Screw 10mm",
      "2802-0004-0010",
      "goBILDA",
      "Hardware",
      "4410 Husky",
      "All",
      50,
      null, null,
      "screw", "bolt"
  ),
  nutDriver = new PartType(
      "5.5MM NUT DRIVER",
      "REV-41-1119",
      "REV",
      "Tools",
      "5273 Husky",
      "All",
      3,
      null, null,
      "screwdriver", "wrench"
  );

  public PersistenceTest() throws IOException {
  }


  @Test
  public void additionChangesState() {
    gateway.change(changer, new Addition(nutDriver));
    var state = gateway.getCurrentState().toArray();

    PartType[] expectedState = { nutDriver };
    assertArrayEquals(expectedState, state);
  }
}
