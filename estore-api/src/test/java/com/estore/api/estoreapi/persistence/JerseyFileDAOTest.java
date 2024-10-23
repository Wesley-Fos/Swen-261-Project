package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.Model.Jersey;
import com.estore.api.estoreapi.Persistence.JerseyFileDAO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class JerseyFileDAOTest{
    JerseyFileDAO jerseyFileDAO;
    Jersey[] testJerseys;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupJerseyFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        String[] ex1 = {"username","text"};
        String[] ex2 = {"username", "text"};

        String[][] reviews = {ex1, ex2};
        testJerseys = new Jersey[3]; // need to fill with jerseys
        testJerseys[0] = new Jersey(1, "John", 1, "red", "large", 10, 10, 5, reviews, "john.png");
        testJerseys[1] = new Jersey(2, "Bob", 2, "blue", "medium", 20, 20, 5, reviews, "bob.png");
        testJerseys[2] = new Jersey(3, "Meredith", 2, "green", "small", 30, 30, 5, reviews, "meredith.png");


        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Jersey[].class))
                .thenReturn(testJerseys);
        jerseyFileDAO = new JerseyFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetJerseys() throws IOException {
        // Invoke
        Jersey[] jerseys = jerseyFileDAO.getJerseys();

        // Analyze
        assertEquals(jerseys.length,testJerseys.length);
        for (int i = 0; i < testJerseys.length;++i)
            assertEquals(jerseys[i],testJerseys[i]);
    }
  
    @Test
    public void testGetJersey() throws IOException {
        // Invoke
        Jersey jersey = jerseyFileDAO.getJersey(1);

        // Analzye
        assertEquals(jersey,testJerseys[0]);
    }

    @Test
    public void testGetJerseyNotFound() throws IOException {
        // Invoke
        Jersey jersey = jerseyFileDAO.getJersey(99);

        // Analyze
        assertEquals(jersey,null);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the HeroFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Jersey[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new JerseyFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testFindJerseys() throws IOException{
        Jersey[] jerseys = jerseyFileDAO.findJerseys(("o"));

        assertEquals(2, jerseys.length);
        assertEquals(jerseys[0], testJerseys[0]);
        assertEquals(jerseys[1], testJerseys[1]);
    }

    @Test
    public void testFindJerseysNone() throws IOException{
        Jersey[] jerseys = jerseyFileDAO.findJerseys(("Sairus"));

        assertEquals(0, jerseys.length);
    }

    @Test
    public void testCreateJersey() throws IOException
    {
        String[] ex1 = {"username","text"};
        String[] ex2 = {"username", "text"};

        String[][] reviews = {ex1, ex2};
        Jersey expected = new Jersey(4, "Josh Allen", 17, "winning", "King", 90000000, 1, 5, reviews, "JoshAllenMyKing.png");
        Jersey actual = jerseyFileDAO.createJersey(expected);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPlayerName(), actual.getPlayerName());
        assertEquals(expected.getPrice(), actual.getPrice());
    }
    
    @Test
    public void testUpdateJersey() throws IOException
    {
        //setup
        String[] ex1 = {"username","text"};
        String[] ex2 = {"username", "text"};

        String[][] reviews = {ex1, ex2};
        Jersey expected = new Jersey(1, "Tim", 0, "burple", "Large", 0, 1, 5, reviews, "tim.jpg");
        //invoke
        jerseyFileDAO.updateJersey(expected);
        Jersey acctual = jerseyFileDAO.getJersey(1);
        //analyze
        assertEquals(expected.getPlayerName(), acctual.getPlayerName());
        assertEquals(expected.getPlayerNumber(), acctual.getPlayerNumber());
        assertEquals(expected.getColor(), acctual.getColor());
    }

    @Test
    public void testUpdateJerseyFail() throws IOException
    {
        //setup
        Jersey expected = null;
        //invoke
        Jersey acctual = jerseyFileDAO.updateJersey(new Jersey(99, null, 0, null, null, 0, 0, 0, null, null));
        //analyze
        assertEquals(expected, acctual);
    }

    @Test
    public void testDeleteJersey() throws IOException 
    {
        // Setup
        boolean expected = true;
        int jerseyId = 1;

        // Invoke
        boolean actual = jerseyFileDAO.deleteJersey(jerseyId);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteJerseyFalse() throws IOException 
    {
        // Setup
        boolean expected = false;
        int jerseyId = 5; // does not exist

        // Invoke
        boolean actual = jerseyFileDAO.deleteJersey(jerseyId);

        // Analyze
        assertEquals(expected, actual);
    }

}
