package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Model.ShoppingCart;

@Tag("Model-tier")
public class ShoppingCartTest {
    
    @Test
    public void testCtor(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);

        ShoppingCart cart2 = new ShoppingCart(55, cart);

        assertEquals(55, cart2.getId());
        assertEquals(expected_customerName, cart2.getCustomerUsername());
        assertEquals(expected_items, cart2.getItems());

    }

    @Test
    public void testCtor2(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);

        String expectedItemsString = "[1,1],[2,5]";
        String str = expected_id + ", " + expected_customerName + ", " + expectedItemsString;

        assertEquals(str, cart.toString());
    }
    
    @Test
    public void testAddItem(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);

        cart.addItem(5, 5);

        String expectedItemsString = "[1,1],[2,5],[5,5]";
        assertEquals(expectedItemsString, cart.getItemsAsString(cart.getItems()));
    }

    @Test
    public void testAddItem2(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);

        cart.addItem(1, 5);

        String expectedItemsString = "[1,6],[2,5]";
        assertEquals(expectedItemsString, cart.getItemsAsString(cart.getItems()));
    }

    @Test
    public void testToString(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        int[] ex1 = {1,1};
        int[] ex2 = {2,5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);

        // id + ", " + customerUsername + ", " + items
        String expectedItemsString = "[1,1],[2,5]";
        String str = expected_id + ", " + expected_customerName + ", " + expectedItemsString;
        assertEquals(str, cart.toString());
    }

    @Test
    public void testToStringNull(){
        int expected_id = 99;
        String expected_customerName = "John Doe";
        int[][] expected_items = null;

        ShoppingCart cart = new ShoppingCart(expected_id, expected_customerName, expected_items);
        // id + ", " + customerUsername + ", " + items
        String expectedItemsString = "[]";
        String str = expected_id + ", " + expected_customerName + ", " + expectedItemsString;
        assertEquals(str, cart.toString());
    }

}