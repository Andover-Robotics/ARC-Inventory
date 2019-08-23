package com.andoverrobotics.inventory.web.browsing;

import com.andoverrobotics.inventory.PartType;

import java.util.ArrayList;
import java.util.List;

public class BrowsablePartType {
    private String name, part_number, brand, category, location, team, uuid, url, image_url;
    private List<String> keywords;
    private int quantity;

    public BrowsablePartType(PartType original) {
        this.name = original.getName();
        this.part_number = original.getPartNumber();
        this.brand = original.getBrand();
        this.category = original.getCategory();
        this.location = original.getLocation();
        this.team = original.getTeam();
        this.uuid = original.getUuid().toString();
        this.url = original.getUrl().toString();
        this.image_url = original.getImageUrl().toString();
        this.quantity = original.getQuantity();
        this.keywords = new ArrayList<String>();
        this.keywords.addAll(original.getKeywords());
    }

    public String getName() {
        return name;
    }

    public String getPart_number() {
        return part_number;
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

    public String getUuid() {
        return uuid;
    }

    public String getUrl() {
        return url;
    }

    public String getImage_url() {
        return image_url;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public int getQuantity() {
        return quantity;
    }
}
