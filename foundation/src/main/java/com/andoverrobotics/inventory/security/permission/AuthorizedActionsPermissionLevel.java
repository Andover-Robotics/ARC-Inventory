package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;

public abstract class AuthorizedActionsPermissionLevel implements PermissionLevel {
  private Action[] authorizedActions;

  AuthorizedActionsPermissionLevel(Action... actions) {
    authorizedActions = actions;
  }

  @Override
  public boolean canPerform(Action someAction) {
    for (var action : authorizedActions) {
      if (action == someAction) {
        return true;
      }
    }
    return false;
  }
}
