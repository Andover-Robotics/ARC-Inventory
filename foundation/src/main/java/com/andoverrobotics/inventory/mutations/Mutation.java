package com.andoverrobotics.inventory.mutations;

import com.andoverrobotics.inventory.PartType;

import java.util.Collection;

/*
  Represents a singular change that can be applied to the inventory.
 */
public interface Mutation {
  void apply(Collection<PartType> parts);
}
