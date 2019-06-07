package com.andoverrobotics.inventory.security;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

public class IdentityTest {
  private MockIdentity mock = new MockIdentity($ -> false);

  @Test
  public void doesNotEqualObjectOfAnotherType() {
    assertNotEquals(mock, new Object());
  }

  @Test
  public void doesNotEqualNull() {
    assertNotEquals(mock, null);
  }

  @Test
  public void equalsGivenIdentity() {
    var other = new MockIdentity($ -> true);
    mock.setFunction(x -> x == other);

    assertEquals(mock, other);
    mock.verifyCalled();

    assertNotEquals(mock, new MockIdentity($ -> Math.random() < 0.5));
    mock.verifyCalled();
  }
}

class MockIdentity extends Identity {
  private boolean compareCalled = false;
  private Predicate<Identity> function;

  MockIdentity(Predicate<Identity> function) {
    this.function = function;
  }

  public void setFunction(Predicate<Identity> function) {
    this.function = function;
  }

  @Override
  protected boolean isIdenticalTo(Identity other) {
    compareCalled = true;
    return function.test(other);
  }

  protected void verifyCalled() {
    assertTrue("expected call to isIdenticalTo, but not called", compareCalled);
    compareCalled = false;
  }
}