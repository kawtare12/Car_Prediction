package com.example.Annonce.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "annonce")
public class Annonce {

    @Id
    private String id;
    private String mileage;
    private String seats;
    private String doors;
    private String horsepower;
    private String fuelType;
    private String carColor;
    private String carBrand;
    private String Description;
    private byte[] image; // Stockage de l'image sous forme binaire
    private String userId; // ID de l'utilisateur connecté
    private String price; // Nouveau champ pour le prix
    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPrice() {
        return price; // Getter pour le prix
    }

    public void setPrice(String price) {
        this.price = price; // Setter pour le prix
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(String horsepower) {
        this.horsepower = horsepower;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }




    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String otherOption) {
        this.carBrand = otherOption;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
