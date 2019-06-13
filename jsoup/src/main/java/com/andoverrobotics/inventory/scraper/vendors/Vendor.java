package com.andoverrobotics.inventory.scraper.vendors;

import com.andoverrobotics.inventory.ScraperGateway;
import org.jsoup.nodes.Document;

import java.net.URL;

public interface Vendor {

  Vendor[] VENDORS = {
      new Tetrix(),
      new GoBILDA(),
      new REV(),
      new AndyMark()
  };

  String getTopLevelDomain();
  ScraperGateway.Interpretation interpret(Document document, URL url) throws Exception;
}
