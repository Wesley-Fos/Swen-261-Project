package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.Controller.ShoppingCartController;
import com.estore.api.estoreapi.Model.ShoppingCart;
import com.estore.api.estoreapi.Persistence.ShoppingCartDAO;


public class ShoppingCartControllerTest {
    private ShoppingCartController shoppingCartController;
    private ShoppingCartDAO mockShoppingCartDAO;

    /**
     * Before each test, create a new shoppingCartController object and inject
     * a mock shoppingcart DAO
     */
    @BeforeEach
    public void setupShoppingCartController(){
        mockShoppingCartDAO = mock(ShoppingCartDAO.class);
        shoppingCartController = new ShoppingCartController(mockShoppingCartDAO);
    }

    @Test
    public void testGetShoppingCart() throws IOException {  // getShoppingCart may throw IOException
        // Setup
        int[][] array = new int[0][0];
        ShoppingCart cart = new ShoppingCart(1, "john", array);
        when(mockShoppingCartDAO.getCart(cart.getId())).thenReturn(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByID(cart.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetShoppingCartNotFound() throws IOException {  // getShoppingCartByID may throw IOException
        // Setup
        int id = 1000;
        when(mockShoppingCartDAO.getCart(id)).thenReturn(null);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByID(id);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartError() throws IOException {
        // Setup
        int id = 1;
        doThrow(new IOException()).when(mockShoppingCartDAO).getCart(id);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByID(id);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetShoppingCarts() throws IOException {
        int[][] array = new int[0][0];
        ShoppingCart cart = new ShoppingCart(1, "john", array);
        ShoppingCart cart2 = new ShoppingCart(1, "john2", array);
        ShoppingCart cart3 = new ShoppingCart(1, "john3", array);
        ShoppingCart[] carts = new ShoppingCart[]{cart,cart2,cart3};
        when(mockShoppingCartDAO.getCarts()).thenReturn(carts);

        // Invoke
        ResponseEntity<ShoppingCart[]> response = shoppingCartController.getShoppingCarts();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(carts, response.getBody());
    }

    @Test
    public void testGetShoppingCartsNotFound() throws IOException {
        ShoppingCart[] carts = null;
        when(mockShoppingCartDAO.getCarts()).thenReturn(carts);

        // Invoke
        ResponseEntity<ShoppingCart[]> response = shoppingCartController.getShoppingCarts();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(carts, response.getBody());
    }

    @Test
    public void testGetShoppingCartsError() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockShoppingCartDAO).getCarts();

        // Invoke
        ResponseEntity<ShoppingCart[]> response = shoppingCartController.getShoppingCarts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartByUsername() throws IOException {
        // Setup
        String searchName = "John";
        int[][] array = new int[0][0];
        ShoppingCart cart = new ShoppingCart(1, "john", array);
        when(mockShoppingCartDAO.getShoppingCart(searchName)).thenReturn(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByUsername(searchName);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetShoppingCartByUsernameNotFound() throws IOException {
        // Setup
        String searchName = "null";
        when(mockShoppingCartDAO.getShoppingCart(searchName)).thenReturn(null);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByUsername(searchName);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartByUsernameError() throws IOException {
        // Setup
        doThrow(new IOException()).when(mockShoppingCartDAO).getShoppingCart(null);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.getShoppingCartByUsername(null);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateShoppingCart() throws IOException {  // createShoppingCart may throw IOException
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        when(mockShoppingCartDAO.createShoppingCart(cart)).thenReturn(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testCreateShoppingCartFailed() throws IOException {  // createShoppingCart may throw IOException
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        when(mockShoppingCartDAO.createShoppingCart(cart)).thenReturn(null);
        System.out.println(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateShoppingCartHandleException() throws IOException {  // createShoppingCart may throw IOException
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        doThrow(new IOException()).when(mockShoppingCartDAO).createShoppingCart(cart);

        // Invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.createShoppingCart(cart);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteShoppingCart() throws IOException {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        when(mockShoppingCartDAO.deleteShoppingCart(cart.getId())).thenReturn(true);
        when(mockShoppingCartDAO.getCart(cart.getId())).thenReturn(cart);

        // invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.deleteShoppingCart(cart.getId());

        // analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteShoppingCartNotFound() throws IOException {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        when(mockShoppingCartDAO.deleteShoppingCart(cart.getId())).thenReturn(false);

        // invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.deleteShoppingCart(cart.getId());

        // analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteShoppingCartHandleException() throws IOException {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        doThrow(new IOException()).when(mockShoppingCartDAO).deleteShoppingCart(cart.getId());
        when(mockShoppingCartDAO.getCart(cart.getId())).thenReturn(cart);

        // invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.deleteShoppingCart(cart.getId());

        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateShoppingCart() throws IOException {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);
        
        when(mockShoppingCartDAO.updateShoppingCart(cart)).thenReturn(cart);
        when(mockShoppingCartDAO.getCart(1)).thenReturn(cart);
        ResponseEntity<ShoppingCart> response = shoppingCartController.updateShoppingCart(cart);
        cart.addItem(1, 5);

        // invoke
        response = shoppingCartController.updateShoppingCart(cart);

        // analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testUpdateShoppingCartFailed() throws IOException { // updateHero may throw IOException
        // setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);

        when(mockShoppingCartDAO.updateShoppingCart(cart)).thenReturn(null);

        // invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.updateShoppingCart(cart);

        // analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateShoppingCartHandleException() throws IOException { // updateHero may throw IOException
        // setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] expected_items = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1, "John Doe", expected_items);

        doThrow(new IOException()).when(mockShoppingCartDAO).updateShoppingCart(cart);
        when(mockShoppingCartDAO.getCart(1)).thenReturn(cart);

        // invoke
        ResponseEntity<ShoppingCart> response = shoppingCartController.updateShoppingCart(cart);

        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}