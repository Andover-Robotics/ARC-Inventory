package com.andoverrobotics.inventory.security;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GoogleIdentityTest {
  private GoogleIdentity identity = new GoogleIdentity("1000012302012908",
      "michael@andoverrobotics.com", "Michael Peng", "http://ungif.it");

  @Test
  public void compareByUserId() {
    var otherIdentity = new GoogleIdentity(identity.userId, "daniel@andoverrobotics.com",
        "Daniel Ivanovich", "http://ivanovich.us");

    assertTrue(identity.isIdenticalTo(otherIdentity));
  }

  @Test
  public void notIdenticalToAnotherType() {
    var otherIdentity = mock(Identity.class);

    assertFalse(identity.isIdenticalTo(otherIdentity));
  }
}