package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;

public class Administrator implements PermissionLevel {
  @Override
  public boolean canPerform(Action someAction) {
    return true;
  }
}
