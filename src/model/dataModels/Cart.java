package model.dataModels;

import java.util.ArrayList;

/**
 * Data model that is used by the CartHandler class as a means
 * to have access to the products stored locally in the user's cart.
 */
public class Cart {
    private ArrayList<Product> cart;
    public Cart(){
        this.cart = new ArrayList<>();
    }

    public ArrayList<Product> getCart(){
        return this.cart;
    }
}
