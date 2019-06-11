package com.andoverrobotics.inventory.security;

public class AnonymousIdentity extends Identity {
  @Override
  protected boolean isIdenticalTo(Identity other) {
    return this == other;
  }
}
