package com.andoverrobotics.inventory.scraper.vendors;

import com.andoverrobotics.inventory.ScraperGateway;
import org.jsoup.nodes.Document;

import java.net.URL;

public class GoBILDA implements Vendor {
  @Override
  public String getTopLevelDomain() {
    return "gobilda.com";
  }

  @Override
  public ScraperGateway.Interpretation interpret(Document document, URL url) throws Exception {
    var name = document.select("*[itemprop='name']").get(0).text();
    var partNumber = document.select("*[data-product-sku]").get(0).child(0).attr("value");
    var imageUrl = document.getElementsByClass("image-1").get(0).attr("abs:src");

    return new ScraperGateway.Interpretation(name, partNumber, "goBILDA", url, new URL(imageUrl));
  }
}
