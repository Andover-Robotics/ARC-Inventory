package com.andoverrobotics.inventory.scraper;

import com.andoverrobotics.inventory.ScraperGateway;
import com.andoverrobotics.inventory.scraper.vendors.Vendor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

public class JsoupScraper implements ScraperGateway {
  @Override
  public Interpretation interpret(URL url) throws Exception {
    for (var vendor : Vendor.VENDORS) {
      if (url.getHost().toLowerCase().contains(vendor.getTopLevelDomain())) {

        Document doc = Jsoup.parse(url, 10000);
        return vendor.interpret(doc, url);

      }
    }

    return null;
  }
}
