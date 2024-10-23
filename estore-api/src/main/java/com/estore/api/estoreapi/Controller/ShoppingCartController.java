package com.estore.api.estoreapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.estore.api.estoreapi.Persistence.ShoppingCartDAO;
import com.estore.api.estoreapi.Model.ShoppingCart;

@RestController
@RequestMapping("estore/shoppingcart")
public class ShoppingCartController {
    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    private ShoppingCartDAO cartDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param cartDao The {@link ShoppingCartDAO Shopping Cart Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public ShoppingCartController(ShoppingCartDAO cartDao) {
        this.cartDao = cartDao;
    }

    /**
     * Creates a {@linkplain ShoppingCart shoppingCart} with the provided ShoppingCart object
     * 
     * @param shoppingCart - The {@link ShoppingCart shoppingCart} to create
     * 
     * @return ResponseEntity with created {@link ShoppingCart shoppingCart} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link ShoppingCart shoppingCart} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart cart) {
        LOG.info("POST /estore/shoppingcart " + cart);

        // Replace below with your implementation
        try{
            if(cartDao.getCart(cart.getId()) != null) return new ResponseEntity<>(HttpStatus.CONFLICT);
            ShoppingCart shoppingCart = cartDao.createShoppingCart(cart);
            if(shoppingCart == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<ShoppingCart>(shoppingCart, HttpStatus.CREATED);
        }   
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Responds to the GET request for all {@linkplain ShoppingCart ShoppingCarts}
     * 
     * @return ResponseEntity with array of {@link ShoppingCart ShoppingCarts} objects or Null and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * @throws IOException
     */
    @GetMapping("/")
    public ResponseEntity<ShoppingCart[]> getShoppingCarts() {
        LOG.info("Get /estore/shoppingcart");
        try{
            ShoppingCart[] shoppingCarts = cartDao.getCarts();
            if (shoppingCarts != null)
                return new ResponseEntity<ShoppingCart[]>(shoppingCarts, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for {@linkplain ShoppingCart ShoppingCarts} with a given ID
     * 
     * @return ResponseEntity witt {@link ShoppingCart ShoppingCarts} objects or Null and
     * HTTP status of OK<br>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCartByID(@PathVariable int id) {
        LOG.info("get /estore/shoppingcart/"+id);
        try{
            ShoppingCart cart = cartDao.getCart(id);
            if (cart != null)
                return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for {@linkplain ShoppingCart ShoppingCarts} with a given ID
     * 
     * @return ResponseEntity witt {@link ShoppingCart ShoppingCarts} objects or Null and
     * HTTP status of OK<br>
     */
    @GetMapping(value = "/", params = "username")
    public ResponseEntity<ShoppingCart> getShoppingCartByUsername(@RequestParam String username) {
        LOG.info("GET /estore/shoppingcart/?username="+username);
        try{
            ShoppingCart cart = cartDao.getShoppingCart(username);
            if (cart != null)
                return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Deletes a {@linkplain ShoppingCart cart} with the given id
     * 
     * @param id The id of the {@link ShoppingCart cart} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ShoppingCart> deleteShoppingCart(@PathVariable int id) {
        LOG.info("DELETE /estore/" + id);

        try {
            if (cartDao.getCart(id) != null) {
                cartDao.deleteShoppingCart(id);
                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain ShoppingCart cart} with the provided {@linkplain ShoppingCart cart} object, if it exists
     * 
     * @param cart The {@link ShoppingCart cart} to update
     * 
     * @return ResponseEntity with updated {@link ShoppingCart cart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<ShoppingCart> updateShoppingCart(@RequestBody ShoppingCart cart) {
        LOG.info("PUT /estore/shoppingcart " + cart.getId() + ", "+cart.getCustomerUsername());

        try {
            if (cartDao.getCart(cart.getId()) != null) {
                cartDao.updateShoppingCart(cart);
                return new ResponseEntity<>(cartDao.getCart(cart.getId()), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

} 