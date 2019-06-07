package com.andoverrobotics.inventory.security;

public class UnauthorizedException extends SecurityException {
  public UnauthorizedException(Action attemptedAction, Identity originator) {
    super(originator + " attempted to " + attemptedAction + " without sufficient privileges");
  }
}
