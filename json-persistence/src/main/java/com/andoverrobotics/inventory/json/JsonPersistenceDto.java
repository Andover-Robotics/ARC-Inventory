package com.andoverrobotics.inventory.json;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.security.AuditLogItem;
import com.andoverrobotics.inventory.security.GoogleIdentity;
import com.andoverrobotics.inventory.security.Identity;

import java.time.LocalDateTime;
import java.util.List;

public class JsonPersistenceDto {

  public static class WhitelistItem {
    public String email;
    public String adderEmail;
    public LocalDateTime timestamp;
  }

  public static class AuditLogItemDto implements AuditLogItem {
    public GoogleIdentity initiator;
    public Mutation change;
    public LocalDateTime time;

    public AuditLogItemDto(GoogleIdentity initiator, Mutation change, LocalDateTime time) {
      this.initiator = initiator;
      this.change = change;
      this.time = time;
    }

    // Required for Gson construction
    public AuditLogItemDto() {}

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
      return time;
    }
  }

  public List<AuditLogItemDto> auditLog;
  public List<WhitelistItem> whitelist;
}
