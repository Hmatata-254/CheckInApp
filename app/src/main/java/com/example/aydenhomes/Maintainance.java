package com.example.aydenhomes;

public class Maintainance {

    private String id;
    private String description;
    private String category;
    private String imageUrl;

    public Maintainance() {
    }

    public Maintainance(String id, String description, String category, String imageUrl) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
