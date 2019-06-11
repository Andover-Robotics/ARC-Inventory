package com.andoverrobotics.inventory.query;

import com.andoverrobotics.inventory.PartType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SearchQuery implements FilterQuery {
  public final String[] tokens;

  public SearchQuery(String query) {
    this.tokens = query.split("\\s");
  }

  @Override
  public boolean matches(PartType part) {
    for (String token : tokens) {
      for (var attribute : partAttributes(part)) {
        if (attribute.get().toLowerCase().contains(token.toLowerCase()))
          return true;
      }
    }
    return false;
  }

  private List<Supplier<String>> partAttributes(PartType part) {
    List<Supplier<String>> list = new LinkedList<>(Arrays.asList(
        part::getName,
        part::getPartNumber,
        part::getBrand,
        part::getCategory,
        part::getLocation
    ));

    for (var keyword : part.getKeywords()) {
      list.add(() -> keyword);
    }

    return list;
  }
}
