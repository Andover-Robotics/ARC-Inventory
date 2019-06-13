package com.andoverrobotics.inventory.scraper.vendors;

import com.andoverrobotics.inventory.ScraperGateway;
import org.jsoup.nodes.Document;

import java.net.URL;

public class REV implements Vendor {
  @Override
  public String getTopLevelDomain() {
    return "revrobotics.com";
  }

  @Override
  public ScraperGateway.Interpretation interpret(Document document, URL url) {
    var name = document.select("*[itemprop='name']").get(0).text();
    var sku = document.select("*[data-product-sku]").get(0).text();
    // These images load by JS. This would yield a loading circle
    //var imageUrl = document.select("*[data-main-image]").get(0).attr("abs:src");

    return new ScraperGateway.Interpretation(name, sku, "REV", url, null);
  }
}
