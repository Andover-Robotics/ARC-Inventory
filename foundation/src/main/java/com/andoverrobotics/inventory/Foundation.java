package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.stream.Stream;

public class Foundation implements FoundationGateway {
  private final PersistenceGateway persistence;
  private final GoogleAccountVerifier accountVerifier;

  public Foundation(PersistenceGateway persistence, GoogleAccountVerifier accountVerifier) {
    this.persistence = persistence;
    this.accountVerifier = accountVerifier;
  }

  @Override
  public Stream<PartType> filter(@Nullable Identity searcher, FilterQuery query) throws UnauthorizedException {
    checkNonNull(query, "FilterQuery");
    ensureAuthorized(searcher, Action.READ_INVENTORY);
    return persistence.filter(query);
  }

  @Override
  public boolean change(Identity changer, Mutation mutation) throws UnauthorizedException {
    return false;
  }

  @Override
  public Identity identify(String idToken) throws GeneralSecurityException, IOException {
    return accountVerifier.verifyIdToken(idToken);
  }

  @Override
  public PermissionLevel permissionLevelOf(Identity identity) {
    if (!(identity instanceof GoogleIdentity)) return PermissionLevel.PUBLIC;
    var googleIdentity = (GoogleIdentity) identity;

    if ("andoverrobotics.com".equals(googleIdentity.hostedDomain))
      return PermissionLevel.ADMIN;

    if (persistence.whitelist().anyMatch(googleIdentity.email::equals))
      return PermissionLevel.WHITELISTED;

    return PermissionLevel.PUBLIC;
  }

  @Override
  public boolean addEmailToWhitelist(Identity changer, String email) throws UnauthorizedException {
    checkNonNull(email, "new whitelist email");
    ensureAuthorized(changer, Action.MANAGE);
    return persistence.addToWhitelist(changer, email);
  }

  @Override
  public boolean removeEmailFromWhitelist(Identity changer, String email) throws UnauthorizedException {
    checkNonNull(email, "whitelist email to remove");
    ensureAuthorized(changer, Action.MANAGE);
    return persistence.removeFromWhitelist(changer, email);
  }

  @Override
  public Stream<String> whitelist(@Nullable Identity viewer) throws UnauthorizedException {
    ensureAuthorized(viewer, Action.MANAGE);
    return persistence.whitelist();
  }

  private void ensureAuthorized(Identity actor, Action action) throws UnauthorizedException {
    if (!permissionLevelOf(actor).canPerform(action))
      throw new UnauthorizedException(action, actor);
  }

  private <T> void checkNonNull(T object, String description) {
    if (object == null)
      throw new IllegalArgumentException(description + " == null");
  }
}
