package com.andoverrobotics.inventory.query;

import com.andoverrobotics.inventory.PartType;

public interface FilterQuery {
  boolean matches(PartType part);
}
