package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;

public interface PermissionLevel {
  boolean canPerform(Action someAction);
}
