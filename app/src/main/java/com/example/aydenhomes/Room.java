package com.example.aydenhomes;

import java.util.Map;

public class Room {

    private String id;
    private String rentPrice;
    private String type;
    private String status;
    private String description;
    private Map<String,String> images;

    public Room() {
    }

    public Room(String id, String rentPrice, String type, String status, String description, Map<String, String> images) {
        this.id = id;
        this.rentPrice = rentPrice;
        this.type = type;
        this.status = status;
        this.description = description;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
