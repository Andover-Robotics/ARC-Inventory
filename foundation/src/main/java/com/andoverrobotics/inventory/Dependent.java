package com.andoverrobotics.inventory;

public interface Dependent<T> {
  void bind(T dependency);
}
