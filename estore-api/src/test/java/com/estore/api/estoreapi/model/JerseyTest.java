package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Model.Jersey;

@Tag("Model-tier")
public class JerseyTest {
    @Test
    public void testCtor(){
        int expected_id = 99;
        String expected_playerName = "John Doe";
        int expected_playerNumber = 55;
        String expected_color = "White";
        String expected_size = "M";
        Double expected_price = 10.99;
        int expected_quantity = 99;
        String expected_imagePath = "model/test/imagepath.jpg";
        Double expected_avgReview = 5.0;
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] expected_reviews = {ex1, ex2};


        Jersey jersey = new Jersey(expected_id, expected_playerName, expected_playerNumber, expected_color, expected_size, expected_price, expected_quantity, expected_avgReview, expected_reviews, expected_imagePath);

        assertEquals(expected_id, jersey.getId());
        assertEquals(expected_playerName, jersey.getPlayerName());
        assertEquals(expected_playerNumber, jersey.getPlayerNumber());
        assertEquals(expected_color, jersey.getColor());
        assertEquals(expected_size, jersey.getSize());
        assertEquals(expected_price, jersey.getPrice());
        assertEquals(expected_quantity, jersey.getQuantity());
        assertEquals(expected_avgReview, jersey.getAvgReview());
        assertEquals(expected_reviews, jersey.getReviews());
        assertEquals(expected_imagePath, jersey.getImagePath());
    }

    @Test
    public void testConstructor(){
        int expected_id = 99;
        String expected_playerName = "John Doe";
        int expected_playerNumber = 55;
        String expected_color = "White";
        String expected_size = "M";
        Double expected_price = 10.99;
        int expected_quantity = 99;
        String expected_imagePath = "model/test/imagepath.jpg";
        Double expected_avgReview = 5.0;
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] expected_reviews = {ex1, ex2};

        Jersey jersey = new Jersey(expected_id, expected_playerName, expected_playerNumber, expected_color, expected_size, expected_price, expected_quantity, expected_avgReview, expected_reviews, expected_imagePath);

        Jersey jersey2 = new Jersey(1, jersey);
        assertEquals(1, jersey2.getId());
        assertEquals(expected_playerName, jersey2.getPlayerName());
        assertEquals(expected_playerNumber, jersey2.getPlayerNumber());
        assertEquals(expected_color, jersey2.getColor());
        assertEquals(expected_size, jersey2.getSize());
        assertEquals(expected_price, jersey2.getPrice());
        assertEquals(expected_quantity, jersey2.getQuantity());
        assertEquals(expected_avgReview, jersey.getAvgReview());
        assertEquals(expected_reviews, jersey.getReviews());
        assertEquals(expected_imagePath, jersey2.getImagePath());
    }
}
