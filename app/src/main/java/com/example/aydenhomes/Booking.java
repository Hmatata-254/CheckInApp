package com.example.aydenhomes;

public class Booking {

    private String roomType;
    private String rentPrice;
    private String image;


    public Booking() {
    }

    public Booking(String roomType, String rentPrice, String image) {
        this.roomType = roomType;
        this.rentPrice = rentPrice;
        this.image = image;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
