package com.andoverrobotics.inventory.security;

public class GoogleIdentity extends Identity {
  // whereas getters would become untested functions, declaring the fields this way ensures that no untested logic can interfere
  // in the retrieval of these attributes. GoogleIdentity is much like a struct in C.
  public final String userId, email, name, pictureUrl, hostedDomain;

  public GoogleIdentity(String userId, String email, String name, String pictureUrl, String hostedDomain) {
    this.userId = userId;
    this.email = email;
    this.name = name;
    this.pictureUrl = pictureUrl;
    this.hostedDomain = hostedDomain;
  }

  public GoogleIdentity(String userId, String email, String name, String pictureUrl) {
    this(userId, email, name, pictureUrl, null);
  }

  @Override
  protected boolean isIdenticalTo(Identity other) {
    if (!(other instanceof GoogleIdentity)) return false;
    return userId.equals(((GoogleIdentity) other).userId);
  }
}
