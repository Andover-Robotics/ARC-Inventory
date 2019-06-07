package com.andoverrobotics.inventory;

import java.net.URL;
import java.util.Set;

public class PartType {
  private String name, partNumber, brand, category, location, team;
  private Set<String> keywords;
  private int quantity;
  private URL url, imageUrl;

  public PartType(String name, String partNumber, String brand, String category,
                  String location, String team, int quantity, URL url, URL imageUrl, String... keywords) {
    this.name = name;
    this.partNumber = partNumber;
    this.brand = brand;
    this.category = category;
    this.location = location;
    this.team = team;
    if (quantity < 0) throw new IllegalArgumentException("quantity < 0");
    this.quantity = quantity;
    this.url = url;
    this.imageUrl = imageUrl;
    this.keywords = Set.of(keywords);
  }

  public String getName() {
    return name;
  }

  public String getPartNumber() {
    return partNumber;
  }

  public String getBrand() {
    return brand;
  }

  public String getCategory() {
    return category;
  }

  public String getLocation() {
    return location;
  }

  public String getTeam() {
    return team;
  }

  public int getQuantity() {
    return quantity;
  }

  public URL getUrl() {
    return url;
  }

  public URL getImageUrl() {
    return imageUrl;
  }

  public Set<String> getKeywords() {
    return keywords;
  }

  public void consume(int amount) {
    if (amount > quantity) throw new IllegalArgumentException(
        "stack underflow: attempted to use " + amount + " of " + name + " when only " + quantity + " available");
    quantity -= amount;
  }
}
