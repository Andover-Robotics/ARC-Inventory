package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.Identity;
import com.andoverrobotics.inventory.security.PermissionLevel;
import com.andoverrobotics.inventory.security.UnauthorizedException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/*
  Gateway to enable the flow of control to travel from the frontend to the foundation.
 */
public interface FoundationGateway {
  Stream<PartType> allParts();
  Stream<PartType> filter(@Nullable Identity searcher, FilterQuery query) throws UnauthorizedException;
  boolean change(Identity changer, Mutation mutation) throws UnauthorizedException;

  Identity identify(String idToken) throws GeneralSecurityException, IOException;
  PermissionLevel permissionLevelOf(Identity identity);

  boolean addEmailToWhitelist(Identity changer, String email) throws UnauthorizedException;
  boolean removeEmailFromWhitelist(Identity changer, String email) throws UnauthorizedException;
  Stream<String> whitelist(@Nullable Identity viewer) throws UnauthorizedException;

  Stream<AuditLogItem> auditLogSince(Identity viewer, LocalDateTime date);
  void rollback(Identity actor, AuditLogItem mostRecentLogItemToKeep) throws IllegalArgumentException, UnauthorizedException;
  void rollback(Identity actor, LocalDateTime instant) throws IllegalArgumentException, UnauthorizedException;
}
