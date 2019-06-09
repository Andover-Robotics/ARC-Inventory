package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.Identity;

import java.util.stream.Stream;

public interface PersistenceGateway {
  Stream<PartType> getCurrentState();
  Stream<PartType> filter(FilterQuery query);
  boolean change(Identity changer, Mutation mutation);
  void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException;

  // Order: first item is oldest, last item is newest
  Stream<AuditLogItem> auditLog();

  boolean addToWhitelist(Identity adder, String newWhitelistItem);
  boolean removeFromWhitelist(Identity remover, String wasWhitelistItem);
  Stream<String> whitelist();
}
