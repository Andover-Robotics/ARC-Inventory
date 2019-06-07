package com.andoverrobotics.inventory.security;

public interface PermissionLevel {
  boolean canPerform(Action someAction);
}
