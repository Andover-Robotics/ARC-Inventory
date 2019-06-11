package com.andoverrobotics.inventory.security;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AnonymousIdentityTest {
  @Test
  public void identityByReferenceEquality() {
    Identity a = new AnonymousIdentity(),
        b = new AnonymousIdentity(),
        c = a,
        d = b;

    assertFalse(a.isIdenticalTo(b));
    assertFalse(b.isIdenticalTo(a));
    assertTrue(a.isIdenticalTo(c));
    assertTrue(d.isIdenticalTo(b));
  }
}