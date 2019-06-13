package com.andoverrobotics.inventory.scraper.vendors;

import com.andoverrobotics.inventory.ScraperGateway;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;

public class Tetrix implements Vendor {
  private Document doc;

  @Override
  public String getTopLevelDomain() {
    return "pitsco.com";
  }

  @Override
  public ScraperGateway.Interpretation interpret(Document document, URL url) throws Exception {
    doc = document;
    var nameElem = itemProp("name").get(0).text();
    var partNumber = itemProp("sku").get(0).text();
    var imageUrl = itemProp("image").get(0).attr("abs:src");

    return new ScraperGateway.Interpretation(nameElem, partNumber, "Tetrix", url, new URL(imageUrl));
  }

  private Elements itemProp(String name) {
    return doc.select("*[itemprop='" + name + "']");
  }
}
