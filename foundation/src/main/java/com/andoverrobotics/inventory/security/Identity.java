package com.andoverrobotics.inventory.security;

import java.io.Serializable;

// Tag class to denote a particular object as a representation of an identity
// This class is meant to be extended by a platform-specific implementation in the frontend module
public abstract class Identity implements Serializable {
  // To enforce custom implementation of comparisons, the following redirects an abstract method to the .equals method

  protected abstract boolean isIdenticalTo(Identity other);

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Identity)) return false;

    var other = (Identity) obj;
    return isIdenticalTo(other);
  }
}
