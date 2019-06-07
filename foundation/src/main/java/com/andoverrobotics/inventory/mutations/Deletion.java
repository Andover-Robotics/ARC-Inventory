package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;
import java.util.NoSuchElementException;

public class Deletion implements Mutation {
  private final String partNumber;

  public Deletion(String partNumber) {
    this.partNumber = partNumber;
  }

  @Override
  public void apply(Collection<PartType> parts) {
    PartType partToRemove = null;

    for (var part : parts) {
      if (part.getPartNumber().equals(partNumber)) {
        partToRemove = part;
      }
    }

    if (partToRemove == null)
      throw new NoSuchElementException(partNumber);
    parts.remove(partToRemove);
  }
}
