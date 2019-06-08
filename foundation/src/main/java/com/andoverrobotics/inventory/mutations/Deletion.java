package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

public class Deletion implements Mutation {
  private final UUID uuid;

  public Deletion(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void apply(Collection<PartType> parts) {
    PartType partToRemove = null;

    for (var part : parts) {
      if (part.getUuid().equals(uuid)) {
        partToRemove = part;
      }
    }

    if (partToRemove == null)
      throw new NoSuchElementException(uuid.toString());
    parts.remove(partToRemove);
  }
}
