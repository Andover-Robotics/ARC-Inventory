package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorizedActionsPermissionLevelTest {

  @Test
  public void canPerformActionsInAuthorizedActions() {
    var level = new MockAuthorizedActions(Action.EDIT_INVENTORY);

    assertTrue(level.canPerform(Action.EDIT_INVENTORY));
  }

  @Test
  public void cannotPerformActionsNotInAuthorizedActions() {
    var level = new MockAuthorizedActions(Action.EDIT_INVENTORY, Action.READ_INVENTORY);

    assertFalse(level.canPerform(Action.MANAGE));
  }
}

class MockAuthorizedActions extends AuthorizedActionsPermissionLevel {
  MockAuthorizedActions(Action... permitted) {
    super(permitted);
  }
}