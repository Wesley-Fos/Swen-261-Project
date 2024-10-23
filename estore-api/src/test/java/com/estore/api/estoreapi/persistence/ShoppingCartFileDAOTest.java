package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Model.ShoppingCart;
import com.estore.api.estoreapi.Persistence.ShoppingCartFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class ShoppingCartFileDAOTest {
    ObjectMapper mockObjectMapper;
    ShoppingCart[] shoppingCarts;
    ShoppingCartFileDAO shoppingCartFileDAO;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupShoppingCartFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        shoppingCarts = new ShoppingCart[3]; // need to fill with carts
        int[][] testData = new int[0][0];
        shoppingCarts[0] = new ShoppingCart(1, "Sally", testData);
        shoppingCarts[1] = new ShoppingCart(2, "Tim", testData);
        shoppingCarts[2] = new ShoppingCart(3, "Colgate", testData);

        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),ShoppingCart[].class))
                .thenReturn(shoppingCarts);
        shoppingCartFileDAO = new ShoppingCartFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetUsername()
    {
        //setup
        ShoppingCart expected = shoppingCarts[0];

        //invoke
        ShoppingCart result = shoppingCartFileDAO.getShoppingCart("Sally");

        //analyze
        assertEquals(expected.getCustomerUsername(), result.getCustomerUsername());
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    public void testGetUsernameNull()
    {
        //setup
        ShoppingCart expected = null;

        //invoke
        ShoppingCart result = shoppingCartFileDAO.getShoppingCart("John");

        //analyze
        assertEquals(expected, result);
    }

    @Test
    public void testGetShoppingCart()
    {
        //setup
        ShoppingCart expected = shoppingCarts[1];

        //invoke
        ShoppingCart result = shoppingCartFileDAO.getCart(2);

        //analyze
        assertEquals(expected.getCustomerUsername(), result.getCustomerUsername());
        assertEquals(expected.getId(), result.getId());
    }


    @Test
    public void testGetShoppingCartFail()
    {
        //setup
        ShoppingCart expected = null;

        //invoke
        ShoppingCart result = shoppingCartFileDAO.getCart(999);

        //analyze
        assertEquals(expected, result);
    }

    @Test
    public void testGetCarts() throws IOException
    {
        ShoppingCart[] expected = shoppingCarts;
        ShoppingCart[] acctual = shoppingCartFileDAO.getCarts();

        assertEquals(expected.length, acctual.length);
        for(int i =0; i < expected.length; i++)
        {
            assertEquals(expected[i], acctual[i]);
        }
    }

    @Test
    public void testGetCartsFail() throws IOException
    {
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),ShoppingCart[].class))
                .thenReturn(new ShoppingCart[0]);
        shoppingCartFileDAO = new ShoppingCartFileDAO("doesnt_matter.txt", mockObjectMapper);

        ShoppingCart[] expected = null;
        ShoppingCart[] acctual = shoppingCartFileDAO.getCarts();

        assertEquals(expected, acctual);
    
    }

    @Test
    public void testCreateShoppingCart() {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] items = {ex1, ex2};
        ShoppingCart cart = new ShoppingCart(1, "Sally", items);

        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartFileDAO.createShoppingCart(cart),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        ShoppingCart actual = shoppingCartFileDAO.getCart(cart.getId());
        assertEquals(actual.getId(),cart.getId());
        assertEquals(actual.getCustomerUsername(),cart.getCustomerUsername());
    }

    @Test
    public void testDeleteShoppingCart() throws IOException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.deleteShoppingCart(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test ShoppingCart array - 1 (because of the delete)
        // Because ShoppingCarts attribute of ShoppingCartFileDAO is package private
        // we can access it directly
        assertEquals((shoppingCartFileDAO.getCarts()).length,shoppingCarts.length-1);
    }

    @Test
    public void testDeleteShoppingCartNotFound() throws IOException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> shoppingCartFileDAO.deleteShoppingCart(99),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(shoppingCartFileDAO.getCarts().length,shoppingCarts.length);
    }

    @Test
    public void testUpdateShoppingCart() {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] testArray = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(1,"John Doe", testArray);

        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartFileDAO.updateShoppingCart(cart),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        ShoppingCart actual = shoppingCartFileDAO.getCart(cart.getId());
        assertEquals(actual,cart);
    }

    @Test
    public void testUpdateShoppingCartNotFound() {
        // Setup
        int[] ex1 = {1,1};
        int[] ex2 = {2, 5};

        int[][] testArray = {ex1, ex2};

        ShoppingCart cart = new ShoppingCart(55,"John Doe", testArray);


        // Invoke
        ShoppingCart result = assertDoesNotThrow(() -> shoppingCartFileDAO.updateShoppingCart(cart),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

} 