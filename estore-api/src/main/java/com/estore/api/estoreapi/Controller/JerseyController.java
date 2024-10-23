package com.estore.api.estoreapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.estore.api.estoreapi.Persistence.JerseyDAO;
import com.estore.api.estoreapi.Model.Jersey;

@RestController
@RequestMapping("estore")
public class JerseyController {
    private static final Logger LOG = Logger.getLogger(JerseyController.class.getName());
    private JerseyDAO jerseyDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param jerseyDao The {@link JerseyDAO Jersey Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public JerseyController(JerseyDAO jerseyDao) {
        this.jerseyDao = jerseyDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Jersey jersey} for the given id
     * 
     * @param id The id used to locate the {@link Jersey jersey}
     * 
     * @return ResponseEntity with {@link Jersey jersey} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Jersey> getJersey(@PathVariable int id) {
        LOG.info("GET /estore/" + id); //Implement
        try{
            Jersey jersey = jerseyDao.getJersey(id);
            if(jersey != null)
                return new ResponseEntity<Jersey>(jersey, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(IOException ioe){
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jerseys} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Jersey[]> getJerseys() {
        LOG.info("GET /estore"); //Implement

        try{
            Jersey[] jerseys = jerseyDao.getJerseys();
            if (jerseys != null)
                return new ResponseEntity<Jersey[]>(jerseys, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(IOException ioe){
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
     * Responds to the GET request for all {@linkplain Jersey jerseys} whose name contains
     * the text in name
     * 
     * @param playerName The name parameter which contains the text used to find the {@link Jersey jerseys}
     * @param size The size parameter which contains the text used to find the {@link Jersey jerseys}
     * @param priceRange The array of numbers parameter which contains the upper[1] and lower[0] enter a value below 0 for either to exclude this search
     * @param playerNum The number parameter which contains the int used to find the {@link Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jersey} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all jerseys that contain the text "ma"
     * GET http://localhost:8080/estore/?playerName=ma
     */
    @GetMapping("/")
    public ResponseEntity<Jersey[]> searchJerseys(@RequestParam String playerName, @RequestParam String size, @RequestParam int[] priceRange, @RequestParam int playerNum, @RequestParam int minReview) {
        LOG.info("GET /estore/?playerName="+playerName+"&size="+size+"&priceRange="+Arrays.toString(priceRange)+"&playerNum="+playerNum+"&minReview="+minReview); //Implement
        try {
            Jersey jerseys[] = jerseyDao.findJerseys(playerName);

            //Checks to see if data is set
            if(size == "" && (priceRange[0] < 0 || priceRange[1] < 0) && playerNum <= 0 && minReview <= 0) new ResponseEntity<Jersey[]>(jerseys,HttpStatus.OK);
            

            if(priceRange[0] < 0 || priceRange[1] < 0) 
            {
                priceRange = new int[2];
                priceRange[0] = Integer.MIN_VALUE;
                priceRange[1] = Integer.MAX_VALUE;
            }
            ArrayList<Jersey> returnArrayList = new ArrayList<>();
            for (Jersey jersey : jerseys) {
                //If jersey is within the set price range
                if((jersey.getPrice() <= priceRange[1] && jersey.getPrice() >= priceRange[0]) 
                    && (jersey.getSize().equalsIgnoreCase(size) || size.equalsIgnoreCase("")) 
                    && (jersey.getPlayerNumber() == playerNum || playerNum <= 0)
                    && (jersey.getAvgReview() >= minReview)){
                        returnArrayList.add(jersey);
                    }
            }
            
            Jersey[] returnArray = new Jersey[returnArrayList.size()];
            returnArrayList.toArray(returnArray);

            return new ResponseEntity<Jersey[]>(returnArray,HttpStatus.OK);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Jersey jersey} with the provided jersey object
     * 
     * @param jersey - The {@link Jersey jersey} to create
     * 
     * @return ResponseEntity with created {@link Jersey jersey} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Jersey jersey} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Jersey> createJersey(@RequestBody Jersey jersey) {
        LOG.info("POST /estore " + jersey);

        // Replace below with your implementation
        try{
            if(jerseyDao.getJersey(jersey.getId()) != null) return new ResponseEntity<>(HttpStatus.CONFLICT);
            jersey = jerseyDao.createJersey(jersey);
            if(jersey == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<Jersey>(jersey, HttpStatus.CREATED);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Updates the {@linkplain Jersey jersey} with the provided {@linkplain Jersey jersey} object, if it exists
     * 
     * @param jersey The {@link Jersey jersey} to update
     * 
     * @return ResponseEntity with updated {@link Jersey jersey} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Jersey> updateJersey(@RequestBody Jersey jersey) {
        LOG.info("PUT /estore " + jersey);

        try {
            if (jerseyDao.getJersey(jersey.getId()) != null) {
                jerseyDao.updateJersey(jersey);
                return new ResponseEntity<>(jerseyDao.getJersey(jersey.getId()), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@link Jersey jersey} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Jersey> deleteJersey(@PathVariable int id) {
        LOG.info("DELETE /estore/" + id);

        try {
            if (jerseyDao.getJersey(id) != null) {
                jerseyDao.deleteJersey(id);
                return new ResponseEntity<>(HttpStatus.OK);

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}