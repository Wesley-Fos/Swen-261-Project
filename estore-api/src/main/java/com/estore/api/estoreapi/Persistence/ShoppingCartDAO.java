package com.estore.api.estoreapi.Persistence;

import java.io.IOException;

import com.estore.api.estoreapi.Model.ShoppingCart;


public interface ShoppingCartDAO {

    /**
     * Creates and saves a {@linkplain ShoppingCart cart}
     * 
     * @param cart {@linkplain ShoppingCart cart} object to be created and saved
     * <br>
     * 
     *
     * @return new {@link ShoppingCart cart} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    ShoppingCart createShoppingCart(ShoppingCart cart) throws IOException;

    /**
     * Gets a {@linkplain ShoppingCart cart}
     * 
     * @param username {@linkplain String username} object username to be retrived
     * <br>
     * 
     *
     * @return {@link ShoppingCart cart} if successful, null otherwise 
     * 
     */
    ShoppingCart getShoppingCart(String username) throws IOException;

        /**
     * Gets a {@linkplain ShoppingCart cart}
     * 
     * @param username {@linkplain Integer id} object username to be retrived
     * <br>
     * 
     *
     * @return {@link ShoppingCart cart} if successful, null otherwise 
     * 
     */
    ShoppingCart getCart(int id) throws IOException;

    /**
     * Gets all {@linkplain ShoppingCart cart}
     * 
     * 
     *
     * @return an array of  all {@link ShoppingCart cart}
     * @throws IOException
     * 
     */
    ShoppingCart[] getCarts() throws IOException;

    /**
     * Deletes a {@linkplain ShoppingCart cart} with the given id
     * 
     * @param id The id of the {@link ShoppingCart cart}
     * 
     * @return true if the {@link ShoppingCart cart} was deleted
     * <br>
     * false if cart with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteShoppingCart(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain ShoppingCart cart}
     * 
     * @param cart {@linkplain ShoppingCart cart} object to be created and saved
     * <br>
     * 
     *
     * @return new {@link ShoppingCart cart} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    ShoppingCart updateShoppingCart(ShoppingCart cart) throws IOException;

}