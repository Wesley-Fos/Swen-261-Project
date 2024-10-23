package com.estore.api.estoreapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Jersey {

    static final String STRING_FORMAT = "Jersey [id=%d, name=%s, playerName=%s, playerNumber=%d, color=%s, size=%s, price=%d, quantity=%d, avgReview=%d, reviews=%s, imagePath=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("playerName") private String playerName;
    @JsonProperty("playerNumber") private int playerNumber;
    @JsonProperty("color") private String color;
    @JsonProperty("size") private String size;
    @JsonProperty("price") private double price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("avgReview") private double avgReview;
    @JsonProperty("reviews") private String[][] reviews;
    @JsonProperty("imagePath") private String imagePath;
   
    public Jersey(@JsonProperty("id") int id, @JsonProperty("playerName") String playerName, @JsonProperty("playerNumber") int playerNumber, @JsonProperty("color") String color, @JsonProperty("size") String size, @JsonProperty("price") double price, @JsonProperty("quantity") int quantity, @JsonProperty("avgReview") double avgReview, @JsonProperty("reviews") String[][] reviews, @JsonProperty("imagePath") String imagePath){
        this.id = id;
        this.playerName = playerName;
        this.playerNumber = playerNumber;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.reviews = reviews;
        this.avgReview = avgReview;
        this.imagePath = imagePath;
    }

    public Jersey(int id, Jersey jersey)
    {
        this.id =id;
        this.playerName = jersey.getPlayerName();
        this.playerNumber = jersey.getPlayerNumber();
        this.color = jersey.getColor();
        this.size = jersey.getSize();
        this.price = jersey.getPrice();
        this.quantity = jersey.getQuantity();
        this.avgReview = jersey.getAvgReview();
        this.reviews = jersey.getReviews();
        this.imagePath = jersey.getImagePath();
    }

    public int getId(){
        return id;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public String getColor(){
        return color;
    }

    public String getSize(){
        return size;
    }

    public Double getPrice(){
        return price;
    }

    public int getQuantity(){
        return quantity;
    }

    public Double getAvgReview(){
        return avgReview;
    }

    public String[][] getReviews(){
        return reviews;
    }

    public String getImagePath(){
        return imagePath;
    }


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getReviewsAsString(String[][] reviews){
        String str = "";
        if(reviews == null || reviews.length == 0) return "[]";
        for(String[] review : reviews){
            str += "[" + review[0] + "," + review[1] + "," + review[2]+ "], ";
        }

        str = str.substring(0, str.length()-2);

        return str;
    }

    @Override
    public String toString(){
        return id + ", " + playerName + ", " + playerNumber + ", " + color + ", " + size + ", " + price + ", " + quantity + ", " + avgReview + "," + getReviewsAsString(getReviews()) + ", " + imagePath;
    }
}