package com.estore.api.estoreapi.Persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.Model.Jersey;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JerseyFileDAO implements JerseyDAO{
     private static final Logger LOG = Logger.getLogger(JerseyFileDAO.class.getName());
    Map<Integer,Jersey> jerseys;   // Provides a local cache of the jersey objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Jersey
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new jersey
    private String filename;    // Filename to read from and write to

    public JerseyFileDAO(@Value("${jerseys.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the jerseys from the file
    }

    private Jersey[] getJerseysArray() {
        return getJerseysArray(null);
    }

    private Jersey[] getJerseysArray(String containsText) {
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();

        for (Jersey jersey : jerseys.values()) {
            if (containsText == null || jersey.getPlayerName().contains(containsText)) {
                jerseyArrayList.add(jersey);
            }
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);
        return jerseyArray;
    }

    private boolean save() throws IOException {
        Jersey[] jerseyArray = getJerseysArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),jerseyArray);
        return true;
    }

    private boolean load() throws IOException {
        jerseys = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of jerseys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Jersey[] jerseyArray = objectMapper.readValue(new File(filename),Jersey[].class);

        // Add each jersey to the tree map and keep track of the greatest id
        for (Jersey jersey : jerseyArray) {
            jerseys.put(jersey.getId(),jersey);
            if (jersey.getId() > nextId)
                nextId = jersey.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
     * Generates the next id for a new {@linkplain Jersey jersey}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    private Jersey[] getJerseyArray(){
        return getJerseyArray(null);
    }

    private Jersey[] getJerseyArray(String containsText){
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();
        for (Jersey j : jerseys.values()){
            if(containsText==null || j.getPlayerName().toLowerCase().contains(containsText.toLowerCase())){
                jerseyArrayList.add(j);
            }
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);

        return jerseyArray;
    }

    @Override
    public Jersey[] getJerseys() throws IOException {
        synchronized(jerseys){
            return getJerseyArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey[] findJerseys(String containsText) throws IOException {
        return getJerseyArray(containsText);
    }

    @Override
    public Jersey getJersey(int id) throws IOException {
        synchronized(jerseys){
            if(jerseys.containsKey(id))
                return jerseys.get(id);
            else
                return null;
        }
    }

    
    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey createJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            Jersey newJersey = new Jersey(nextId(), jersey);
            jerseys.put(newJersey.getId(), newJersey);
            save(); // may throw an IOException
            return newJersey;
        }
    }

    @Override
    public Jersey updateJersey(Jersey jersey) throws IOException {
        synchronized (jerseys) {
            if (!jerseys.containsKey(jersey.getId())) {
                return null;
            }

            jerseys.put(jersey.getId(), jersey);
            save();
            return jersey;
        }
    }

    @Override
    public boolean deleteJersey(int id) throws IOException {
        synchronized (jerseys) {
            if (jerseys.containsKey(id)) {
                jerseys.remove(id);
                return save();
            }

            else {
                return false;
            }
        }
    }
}
