package com.andoverrobotics.inventory;

import java.net.URL;

public interface ScraperGateway {

  class Interpretation {
    public final String name, partNumber, brand;
    public final URL url, imageUrl;

    public Interpretation(String name, String partNumber, String brand, URL url, URL imageUrl) {
      this.name = name;
      this.partNumber = partNumber;
      this.brand = brand;
      this.url = url;
      this.imageUrl = imageUrl;
    }
  }

  Interpretation interpret(URL url) throws Exception;
}
