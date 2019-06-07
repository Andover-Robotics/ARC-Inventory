package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;

public class Consumption implements Mutation {
  private final String partNumber;
  private final int amount;

  public Consumption(String partNumber, int amount) {
    this.partNumber = partNumber;
    this.amount = amount;
  }

  @Override
  public void apply(Collection<PartType> parts) {
    for (var part : parts) {
      if (part.getPartNumber().equals(partNumber)) {
        part.consume(amount);
        return;
      }
    }
    throw new IllegalArgumentException("no part in inventory with part number " + partNumber);
  }
}
