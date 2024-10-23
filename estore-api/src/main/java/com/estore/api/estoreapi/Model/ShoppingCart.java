package com.estore.api.estoreapi.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCart {

    static final String STRING_FORMAT = "ShoppingCart [id=%d, customerUsername=%s, items=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("customerUsername") private String customerUsername;
    @JsonProperty("Items") private int[][] items;

    public ShoppingCart(@JsonProperty("id") int id, @JsonProperty("customerUsername") String customerUsername, @JsonProperty("Items") int[][] items){
        this.id = id;
        this.customerUsername = customerUsername;
        this.items = items;
    }

    public ShoppingCart(int id, ShoppingCart cart)
    {
        this.id = id;
        this.customerUsername = cart.getCustomerUsername();
        this.items = cart.getItems();
    }

    public int getId(){
        return id;
    }

    public String getCustomerUsername(){
        return customerUsername;
    }

    public int[][] getItems(){
        return items;
    }

    public void addItem(int jerseyId, int quantity){
        int[][] itemsArray = getItems();

        boolean found = false;
        for(int[] i : itemsArray){
            if(i[0]==jerseyId){
                i[1] = quantity + 1;
                found = true;
            }
        }

        if(!found){
            int[] newItem = {jerseyId, quantity};
            itemsArray = new int[itemsArray.length+1][2];

            int[][] currentItems = getItems();
            for(int i = 0; i < itemsArray.length; i++){
                if(i == itemsArray.length-1){
                    itemsArray[i] = newItem;
                }else{
                    itemsArray[i] = currentItems[i];
                }
            }
        }

        updateItem(itemsArray);
        
    }

    private void updateItem(int[][] items){
        this.items = items;
    }

    public String getItemsAsString(int[][] items){
        String str = "";
        if(items == null || items.length == 0) return "[]";
        for(int[] item : items){
            str += "[" + item[0] + "," + item[1] + "],";
        }

        str = str.substring(0, str.length()-1);

        return str;
    }

    @Override
    public String toString(){
        return id + ", " + customerUsername + ", " + getItemsAsString(items);
    }
} 