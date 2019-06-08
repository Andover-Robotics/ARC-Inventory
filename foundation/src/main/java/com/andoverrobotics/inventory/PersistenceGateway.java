package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.Identity;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface PersistenceGateway {
  Stream<PartType> getCurrentState();
  Stream<PartType> filter(FilterQuery query);
  boolean change(Identity changer, Mutation mutation);

  Stream<AuditLogItem> getAuditLogSince(LocalDateTime time);
  default Stream<AuditLogItem> getFullAuditLog() {
    return getAuditLogSince(
        LocalDateTime.of(2018, 1, 1, 12, 0));
  }

  boolean addToWhitelist(Identity adder, String newWhitelistItem);
  boolean removeFromWhitelist(Identity remover, String wasWhitelistItem);
  Stream<String> whitelist();
}
