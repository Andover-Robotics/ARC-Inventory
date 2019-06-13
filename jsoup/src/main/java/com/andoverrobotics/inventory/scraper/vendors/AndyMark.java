package com.andoverrobotics.inventory.scraper.vendors;

import com.andoverrobotics.inventory.ScraperGateway;
import org.jsoup.nodes.Document;

import java.net.URL;

public class AndyMark implements Vendor {
  @Override
  public String getTopLevelDomain() {
    return "andymark.com";
  }

  @Override
  public ScraperGateway.Interpretation interpret(Document document, URL url) throws Exception {
    var name = document.select("*[itemprop='name']").get(0).text();
    var sku = document.select("*[itemprop='productID']").get(0).text();
    var imageUrl = document.select("*[itemprop='image']").get(0).attr("abs:src");

    return new ScraperGateway.Interpretation(name, sku, "AndyMark", url, new URL(imageUrl));
  }
}
