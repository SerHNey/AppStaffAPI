package com.example.example;

public class Game {
    int id;
    String title;
    int cost;
    int stockAvailability;
    int availabilityInTheStore;
    String description;
    String rewiews;
    String image;


    public Game(int id, String title, int cost, int stockAvailability, int availabilityInTheStore, String description, String rewiews, String image) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.stockAvailability = stockAvailability;
        this.availabilityInTheStore = availabilityInTheStore;
        this.description = description;
        this.rewiews = rewiews;
        this.image = image;
    }
    public Game(String title, int cost, int stockAvailability, int availabilityInTheStore, String description, String rewiews, String image) {

        this.title = title;
        this.cost = cost;
        this.stockAvailability = stockAvailability;
        this.availabilityInTheStore = availabilityInTheStore;
        this.description = description;
        this.rewiews = rewiews;
        this.image = image;
    }
}
