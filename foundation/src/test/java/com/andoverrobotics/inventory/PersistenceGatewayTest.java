package com.andoverrobotics.inventory;

import com.andoverrobotics.inventory.mutations.Mutation;
import com.andoverrobotics.inventory.security.Identity;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

public class PersistenceGatewayTest {

  @Test
  public void getFullAuditLog() {
    PersistenceGateway gateway = new PersistenceGateway() {
      @Override
      public Stream<PartType> getCurrentState() {
        return null;
      }

      @Override
      public Stream<com.andoverrobotics.inventory.PartType> filter(com.andoverrobotics.inventory.query.FilterQuery query) {
        return null;
      }

      @Override
      public boolean change(com.andoverrobotics.inventory.security.Identity changer, Mutation mutation) {
        return false;
      }

      @Override
      public Stream<com.andoverrobotics.inventory.security.AuditLogItem> getAuditLogSince(LocalDateTime time) {
        return time.isBefore(LocalDateTime.of(2019, 6, 6, 10, 49)) ?
            Stream.empty() : null;
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
    };
    assertNotNull(gateway.getFullAuditLog());
  }
}