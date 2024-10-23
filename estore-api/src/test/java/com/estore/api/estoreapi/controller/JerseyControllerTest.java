package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.Controller.JerseyController;
import com.estore.api.estoreapi.Model.Jersey;
import com.estore.api.estoreapi.Persistence.JerseyDAO;

@Tag("Controller-tier")
public class JerseyControllerTest{
    private JerseyController jerseyController;
    private JerseyDAO mockJerseyDAO;

    /**
     * Before each test, create a new jerseyController object and inject
     * a mock jersey DAO
     */
    @BeforeEach
    public void setupJerseyController(){
        mockJerseyDAO = mock(JerseyDAO.class);
        jerseyController = new JerseyController(mockJerseyDAO);
    }

    @Test
    public void testSearchJerseys() throws IOException{
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        String searchString = "uh";
        Jersey[] testJerseys = new Jersey[2];
        testJerseys[0] = new Jersey(1,"Uhchris",1,"red","large",20,10,5,reviews, "path/imagepath");
        testJerseys[1] = new Jersey(2,"Notchris",2,"blue","medium",10,20,5,reviews, "path/imagepath");

        // When findJerseys is called with the search string, return the two
        /// jerseys above
        when(mockJerseyDAO.findJerseys(searchString)).thenReturn(new Jersey[]{testJerseys[0]});

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(searchString, "", new int[]{-3,1}, -3, 0);
        Jersey[] expected = response.getBody();
        
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(1, expected.length);
        assertEquals(testJerseys[0].getPlayerName(), expected[0].getPlayerName());
    }

    @Test
    public void testSearchJerseysSize() throws IOException{
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        String searchString = "";
        Jersey[] testJerseys = new Jersey[2];
        testJerseys[0] = new Jersey(1,"Uhchris",1,"red","large",20,10, 5,reviews, "path/imagepath");
        testJerseys[1] = new Jersey(2,"Notchris",2,"blue","medium",10,20, 5, reviews, "path/imagepath");
        String size = "medium";

        when(mockJerseyDAO.findJerseys(searchString)).thenReturn(testJerseys);

        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(searchString, size, new int[]{-3,1}, -3, 0);
        Jersey[] jerseys = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(1, jerseys.length);
        assertEquals(testJerseys[1].getPlayerName(), jerseys[0].getPlayerName());
    }

    @Test
    public void testSearchJerseysPriceRange() throws IOException{
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        String searchString = "";
        Jersey[] testJerseys = new Jersey[2];
        testJerseys[0] = new Jersey(1,"Uhchris",1,"red","large",20,10, 5, reviews, "path/imagepath");
        testJerseys[1] = new Jersey(2,"Notchris",2,"blue","medium",10,20,5,reviews, "path/imagepath");
        int[] priceRange = new int[]{11,21};

        when(mockJerseyDAO.findJerseys(searchString)).thenReturn(testJerseys);

        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(searchString, "", priceRange, -3, 0);
        Jersey[] jerseys = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(1, jerseys.length);
        assertEquals(testJerseys[0].getPlayerName(), jerseys[0].getPlayerName());
        assertEquals(testJerseys[0].getPlayerName(), jerseys[0].getPlayerName());
    }

    @Test
    public void testSearchJerseysNumber() throws IOException{
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        String searchString = "";
        Jersey[] testJerseys = new Jersey[2];
        testJerseys[0] = new Jersey(1,"Uhchris",1,"red","large",20,10, 5, reviews, "path/imagepath");
        testJerseys[1] = new Jersey(2,"Notchris",2,"blue","medium",10,20, 5, reviews, "path/imagepath");
        int playerNumber = 1;

        when(mockJerseyDAO.findJerseys(searchString)).thenReturn(testJerseys);

        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(searchString, "", new int[]{-3,1}, playerNumber, 0);
        Jersey[] jerseys = response.getBody();
        System.out.println(jerseys);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(1, jerseys.length);
        assertEquals(testJerseys[0].getPlayerName(), jerseys[0].getPlayerName());
        assertEquals(testJerseys[0].getPlayerName(), jerseys[0].getPlayerName());
    }

    @Test
    public void testSearchJerseysHandleException() throws IOException{
        // Setup
        String searchString = "uh";
        doThrow(new IOException()).when(mockJerseyDAO).findJerseys(searchString);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys(searchString, null, new int[]{-3,1}, -3, 0);

        // Anaylze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetJersey() throws IOException {  // getJersey may throw IOException
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey jersey = new Jersey(1, "Ray Bourque", 42, "White", "S", 44.99, 25, 5, reviews, "/somePath.png");
        // When the same id is passed in, our mock Jersey DAO will return the Jersey object
        when(mockJerseyDAO.getJersey(jersey.getId())).thenReturn(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jersey.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jersey,response.getBody());
    }

    @Test
    public void testGetJerseyNotFound() throws Exception { // createJersey may throw IOException
        // Setup
        int jerseyId = 99;
        // When the same id is passed in, our mock Jersey DAO will return null, simulating
        // no jersey found
        when(mockJerseyDAO.getJersey(jerseyId)).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetJerseyError() throws Exception { // createJersey may throw IOException
        // Setup
        int jerseyId = 99;
        // When the same id is passed in, our mock Jersey DAO will return null, simulating
        // no jersey found
        doThrow(new IOException()).when(mockJerseyDAO).getJersey(jerseyId);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


    public void testCreateJersey() throws IOException {  // createJersey may throw IOException
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey jersey = new Jersey(99, "sam", 73, "Blue", "Large", 79.99, 3, 5, reviews, "https.com");
        when(mockJerseyDAO.createJersey(jersey)).thenReturn(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(jersey, response.getBody());
    }

    @Test
    public void testCreateJerseyFailed() throws IOException {  // createJersey may throw IOException
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey jersey = new Jersey(99, "sam", 73, "Blue", "Large", 79.99, 3, 5, reviews, "https.com");
        when(mockJerseyDAO.createJersey(jersey)).thenReturn(null);
        System.out.println(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateJerseyHandleException() throws IOException {  // createJersey may throw IOException
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey jersey = new Jersey(99, "sam", 73, "Blue", "Large", 79.99, 3, 5, reviews, "https.com");
        doThrow(new IOException()).when(mockJerseyDAO).createJersey(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetJerseys() throws IOException { // getJersey may throw IOException
        // Setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey[] jerseys = new Jersey[2];
        jerseys[0] = new Jersey(1, "Ray Bourque", 42, "White", "S", 44.99, 25, 5, reviews, "/somePath.png");
        jerseys[1] = new Jersey(5, "Bobby Orr", 17, "White", "L", 44.99, 25, 5, reviews, "/somePath.png");
        // When getJerseys is called return the jerseys created above
        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys,response.getBody());
    }

    @Test
    public void testGetJerseysEmpty() throws IOException { // getJersey may throw IOException
        // Setup
        Jersey[] jerseys = new Jersey[0];
        // When getJerseys is called return the jerseys created above
        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys,response.getBody());
    }

    @Test
    public void testGetJerseysNull() throws IOException { // getJersey may throw IOException
        // Setup
        Jersey[] jerseys = null;
        // When getJerseys is called return the jerseys created above
        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(jerseys,response.getBody());
    }

    @Test
    public void testGetHeroesHandleException() throws IOException { // getJersey may throw IOException
        // Setup
        // When getJerseys is called on the Mock Jersey DAO, throw an IOException
        doThrow(new IOException()).when(mockJerseyDAO).getJerseys();

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateJersey() throws IOException {
        // setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey testJersey = new Jersey(1, "John Doe", 55, "white", "M", 10.99, 5, 5, reviews, "/some/path");
        when(mockJerseyDAO.updateJersey(testJersey)).thenReturn(testJersey);
        when(mockJerseyDAO.getJersey(1)).thenReturn(testJersey);
        ResponseEntity<Jersey> response = jerseyController.updateJersey(testJersey);
        testJersey.setPlayerName("another test player");

        // invoke
        response = jerseyController.updateJersey(testJersey);

        // analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testJersey,response.getBody());
    }

    @Test
    public void testUpdateJerseyFailed() throws IOException { // updateHero may throw IOException
        // setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey testJersey = new Jersey(99, "random player", 71, "blue", "L", 5.99, 2, 5, reviews, "test/image/path");
        when(mockJerseyDAO.updateJersey(testJersey)).thenReturn(null);

        // invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(testJersey);

        // analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateJerseyHandleException() throws IOException { // updateHero may throw IOException
        // setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        Jersey testJersey = new Jersey(1, "John Doe", 55, "white", "M", 10.99, 5, 5, reviews, "/some/path");
        doThrow(new IOException()).when(mockJerseyDAO).updateJersey(testJersey);
        when(mockJerseyDAO.getJersey(1)).thenReturn(testJersey);

        // invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(testJersey);

        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteJersey() throws IOException {
        // setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        int jerseyId = 1;
        Jersey testJersey = new Jersey(1, "John Doe", 55, "white", "M", 10.99, 5, 5, reviews, "/some/path");
        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(true);
        when(mockJerseyDAO.getJersey(jerseyId)).thenReturn(testJersey);

        // invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteHeroNotFound() throws IOException {
        // setup
        int jerseyId = 99;
        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(false);

        // invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteJerseyHandleException() throws IOException {
        // setup
        String[] ex1 = {"username", "5", "text"};
        String[] ex2 = {"username", "5", "text"};

        String[][] reviews = {ex1, ex2};

        int jerseyId = 1;
        Jersey testJersey = new Jersey(1, "John Doe", 55, "white", "M", 10.99, 5,5, reviews, "/some/path");
        doThrow(new IOException()).when(mockJerseyDAO).deleteJersey(jerseyId);
        when(mockJerseyDAO.getJersey(jerseyId)).thenReturn(testJersey);

        // invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
