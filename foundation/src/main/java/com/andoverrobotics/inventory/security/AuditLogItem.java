package com.andoverrobotics.inventory.security;

import com.andoverrobotics.inventory.mutations.Mutation;

import java.time.LocalDateTime;

public interface AuditLogItem {
  Identity getInitiator();
  Mutation getChange();
  LocalDateTime getTime();
}
