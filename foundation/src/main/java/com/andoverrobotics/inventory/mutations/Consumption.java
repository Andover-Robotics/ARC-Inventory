package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;
import java.util.UUID;

public class Consumption implements Mutation {
  private final UUID uuid;
  private final int amount;

  public Consumption(UUID uuid, int amount) {
    this.uuid = uuid;
    this.amount = amount;
  }

  @Override
  public void apply(Collection<PartType> parts) {
    for (var part : parts) {
      if (part.getUuid().equals(uuid)) {
        part.consume(amount);
        return;
      }
    }
    throw new IllegalArgumentException("no part in inventory with part number " + uuid);
  }
}
