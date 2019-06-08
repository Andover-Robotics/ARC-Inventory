package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.Identity;
import com.andoverrobotics.inventory.security.PermissionLevel;
import com.andoverrobotics.inventory.security.UnauthorizedException;

import java.util.stream.Stream;

/*
  Gateway to enable the flow of control to travel from the frontend to the foundation.
 */
public interface FoundationGateway extends Dependent<PersistenceGateway> {
  Stream<PartType> filter(Identity searcher, FilterQuery query) throws UnauthorizedException;
  boolean change(Identity changer, Mutation mutation) throws UnauthorizedException;

  Identity identify(String idToken);
  PermissionLevel permissionLevelOf(Identity identity);

  boolean addEmailToWhitelist(Identity changer, String email) throws UnauthorizedException;
  boolean removeEmailFromWhitelist(Identity changer, String email) throws UnauthorizedException;
  Stream<String> whitelist(Identity viewer) throws UnauthorizedException;
}
