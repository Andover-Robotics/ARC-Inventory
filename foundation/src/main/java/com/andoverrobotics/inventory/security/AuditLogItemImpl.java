package com.andoverrobotics.inventory.security;

import com.andoverrobotics.inventory.mutations.Mutation;

import java.time.LocalDateTime;

public class AuditLogItemImpl implements AuditLogItem {
  private final Identity initiator;
  private final Mutation change;
  private final LocalDateTime dateTime;

  public AuditLogItemImpl(Identity initiator, Mutation change, LocalDateTime dateTime) {
    this.initiator = initiator;
    this.change = change;
    this.dateTime = dateTime;
  }

  public AuditLogItemImpl(Identity initiator, Mutation change) {
    this(initiator, change, LocalDateTime.now());
  }

  @Override
  public Identity getInitiator() {
    return initiator;
  }

  @Override
  public Mutation getChange() {
    return change;
  }

  @Override
  public LocalDateTime getTime() {
    return dateTime;
  }
}
