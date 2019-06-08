package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;
import org.junit.Test;

import static org.junit.Assert.*;

public class PublicReaderTest {
  private PublicReader reader = new PublicReader();

  @Test
  public void canRead() {
    assertTrue(reader.canPerform(Action.READ_INVENTORY));
  }

  @Test
  public void cannotDoOthers() {
    assertFalse(reader.canPerform(Action.EDIT_INVENTORY));
    assertFalse(reader.canPerform(Action.MANAGE));
  }
}