package com.example.pro2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tourist_attractions")
public class TouristAttraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long touristAttractionId;
    private String name;

    private String price;

    private double rating;

    private String region;

    private String city;

    private String description;

    private String category;

    public TouristAttraction() {
    }

    public TouristAttraction(String name, String price, double rating, String region, String city, String description, String category) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.region = region;
        this.city = city;
        this.description = description;
        this.category = category;
    }

    public Long getTouristAttractionId() {
        return touristAttractionId;
    }

    public void setTouristAttractionId(Long touristAttractionId) {
        this.touristAttractionId = touristAttractionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


}
