package com.andoverrobotics.inventory.security;

import static com.andoverrobotics.inventory.security.Action.EDIT_INVENTORY;
import static com.andoverrobotics.inventory.security.Action.READ_INVENTORY;

public enum PermissionLevel {
  PUBLIC(READ_INVENTORY),
  WHITELISTED(READ_INVENTORY, EDIT_INVENTORY),
  ADMIN(Action.values());

  private final Action[] authorizedActions;

  PermissionLevel(Action... authorizedActions) {
    this.authorizedActions = authorizedActions;
  }

  public boolean canPerform(Action action) {
    for (var act : authorizedActions) {
      if (act == action)
        return true;
    }
    return false;
  }
}
