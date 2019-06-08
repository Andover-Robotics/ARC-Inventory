package com.andoverrobotics.inventory.security.permission;

import com.andoverrobotics.inventory.security.Action;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdministratorTest {
  @Test
  public void canDoAnything() {
    Administrator admin = new Administrator();

    for (var action : Action.values()) {
      assertTrue(admin.canPerform(action));
    }
  }
}