package com.andoverrobotics.inventory.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnauthorizedExceptionTest {
  private Identity identity = new Identity() {
    @Override
    protected boolean isIdenticalTo(Identity other) {
      return false;
    }
  };
  private Action action = Action.EDIT_INVENTORY;

  @Test
  public void messageIsFormattedCorrectly() {
    var exception = new UnauthorizedException(action, identity);
    assertEquals(identity + " attempted to " + action + " without sufficient privileges", exception.getMessage());
  }
}