package com.andoverrobotics.inventory.security;

import org.junit.Test;

import static com.andoverrobotics.inventory.security.Action.*;
import static com.andoverrobotics.inventory.security.PermissionLevel.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PermissionLevelTest {

  @Test
  public void publicReaderCanOnlyRead() {
    assertTrue(PUBLIC.canPerform(READ_INVENTORY));
    assertFalse(PUBLIC.canPerform(EDIT_INVENTORY));
    assertFalse(PUBLIC.canPerform(MANAGE));
  }

  @Test
  public void whitelistedCanEditButNotManage() {
    assertTrue(WHITELISTED.canPerform(READ_INVENTORY));
    assertTrue(WHITELISTED.canPerform(EDIT_INVENTORY));
    assertFalse(WHITELISTED.canPerform(MANAGE));
  }

  @Test
  public void adminCanDoAnything() {
    for (var action : Action.values()) {
      assertTrue(ADMIN.canPerform(action));
    }
  }
}