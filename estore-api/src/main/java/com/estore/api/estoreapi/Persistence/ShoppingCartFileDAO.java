package com.estore.api.estoreapi.Persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.Model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ShoppingCartFileDAO implements ShoppingCartDAO{
     private static final Logger LOG = Logger.getLogger(ShoppingCartFileDAO.class.getName());
    Map<Integer,ShoppingCart> shoppingCarts;   // Provides a local cache of the shoppingcart objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between shoppingcart
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new shoppingcart
    private String filename;    // Filename to read from and write to

    public ShoppingCartFileDAO(@Value("${shoppingCart.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the shoppingcarts from the file
    }

    private ShoppingCart[] getShoppingCartArray() {
        return getShoppingCartArray(-1);
    }

    private ShoppingCart[] getShoppingCartArray(int id) {
        ArrayList<ShoppingCart> shoppingCartArrayList = new ArrayList<>();

        for (ShoppingCart sc : shoppingCarts.values()) {
            if (id == -1 || sc.getId() == id) {
                shoppingCartArrayList.add(sc);
            }
        }

        ShoppingCart[] shoppingCartArray = new ShoppingCart[shoppingCartArrayList.size()];
        shoppingCartArrayList.toArray(shoppingCartArray);
        return shoppingCartArray;
    }

    private boolean save() throws IOException {
        ShoppingCart[] shoppingCartArray = getShoppingCartArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),shoppingCartArray);
        return true;
    }

    private boolean load() throws IOException {
        shoppingCarts = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of ShoppingCarts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        ShoppingCart[] shoppingCartArray = objectMapper.readValue(new File(filename),ShoppingCart[].class);

        // Add each cart to the tree map and keep track of the greatest id
        for (ShoppingCart cart : shoppingCartArray) {
            shoppingCarts.put(cart.getId(),cart);
            if (cart.getId() > nextId)
                nextId = cart.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * Generates the next id for a new {@linkplain ShoppingCart cart}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public ShoppingCart createShoppingCart(ShoppingCart cart) throws IOException {
        synchronized(cart) {
            ShoppingCart newCart = new ShoppingCart(nextId(), cart);
            shoppingCarts.put(newCart.getId(), newCart);
            save(); // may throw an IOException
            return newCart;
        }
    }


    /**
    ** {@inheritDoc}
     */
    @Override
    public ShoppingCart getShoppingCart(String username) {
        for(Map.Entry<Integer, ShoppingCart> cart : shoppingCarts.entrySet())
        {
            if(cart.getValue().getCustomerUsername().equalsIgnoreCase(username)) return cart.getValue();
        }
        return null;
    }


    /**
    ** {@inheritDoc}
     */
    @Override
    public ShoppingCart getCart(int id) {
        if(!shoppingCarts.keySet().contains(id)) 
            return null;
        
        return shoppingCarts.get(id);
    }

    private ShoppingCart[] getCartArray() {
        if(shoppingCarts.size() == 0) return null;
        return new ArrayList<ShoppingCart>(shoppingCarts.values()).toArray(new ShoppingCart[shoppingCarts.values().size()]);
    }

    @Override
    public ShoppingCart[] getCarts() throws IOException {
        synchronized(shoppingCarts){
            return getCartArray();
        }
    }

    @Override
    public boolean deleteShoppingCart(int id) throws IOException {
        synchronized (shoppingCarts) {
            if (shoppingCarts.containsKey(id)) {
                shoppingCarts.remove(id);
                return save();
            }

            else {
                return false;
            }
        }
    }

    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart cart) throws IOException {
        synchronized (shoppingCarts) {
            if (!shoppingCarts.containsKey(cart.getId())) {
                return null;
            }

            shoppingCarts.put(cart.getId(), cart);
            save();
            return cart;
        }
    }

}


