package com.andoverrobotics.inventory.sql;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;

import java.util.stream.Stream;

// schema: inventory
public interface SqlProtocolAgent {
  void initializeTables();

  // oldest to newest
  Stream<Mutation> getSortedMutations();

  boolean mutate(GoogleIdentity mutator, Mutation mutation);
  void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException;

  Stream<AuditLogItem> auditLog();

  boolean addToWhitelist(Identity adder, String newWhitelistItem);
  boolean removeFromWhitelist(Identity remover, String wasWhitelistItem);
  Stream<String> whitelistEmails();
}
