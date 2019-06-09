package com.andoverrobotics.inventory.sql;

import com.andoverrobotics.inventory.PartType;
import com.andoverrobotics.inventory.PersistenceGateway;
import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.query.FilterQuery;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.Identity;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class SqlPersistence implements PersistenceGateway {
  @Override
  public Stream<PartType> getCurrentState() {
    return null;
  }

  @Override
  public Stream<PartType> filter(FilterQuery query) {
    return null;
  }

  @Override
  public boolean change(Identity changer, Mutation mutation) {
    return false;
  }

  @Override
  public void rollback(AuditLogItem mostRecentActiveLogItem) throws IllegalArgumentException {
  }

  @Override
  public Stream<AuditLogItem> auditLogBetween(LocalDateTime start, LocalDateTime end) {
    return null;
  }

  @Override
  public boolean addToWhitelist(Identity adder, String newWhitelistItem) {
    return false;
  }

  @Override
  public boolean removeFromWhitelist(Identity remover, String wasWhitelistItem) {
    return false;
  }

  @Override
  public Stream<String> whitelist() {
    return null;
  }
}
