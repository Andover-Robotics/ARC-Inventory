package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;

public class Addition implements Mutation {
  private final PartType newPart;

  public Addition(PartType newPart) {
    this.newPart = newPart;
  }

  @Override
  public void apply(Collection<PartType> parts) {
    parts.add(newPart);
  }
}
