package controller;

import model.CartHandler;
import model.dataModels.Product;

import java.util.ArrayList;

/**
 * The controller part of the cart which acts as the medium between the View and the Model
 * of the users shopping cart.
 */
public class CartController {
    private CartHandler cartHandler;

    /**
     * Instantiates the CartHandler class
     */
    public CartController(){
        this.cartHandler = new CartHandler();
    }

    /**
     * This method tells the handler to add an item to the locally stored user cart
     * @param itemName - name of the item to be added
     * @param itemQuantity - quantity of the item to be added
     * @param itemPrice - price of the item to be added
     */
    public void addItem(String itemName, int itemQuantity, int itemPrice){
        this.cartHandler.addItem(itemName,itemQuantity,itemPrice);
    }

    /**
     * This method tells the handler to remove an item from the locally stored user cart.
     * @param itemName - name of the method to be added
     * @param itemPrice - price of the method to be added
     */
    public void removeItem(String itemName,int itemPrice){
        this.cartHandler.removeItem(itemName,itemPrice);
    }

    /**
     * This method tells the handler to remove all items from the locally stored user cart.
     */
    public void clearContents(){
        this.cartHandler.clearContents();
    }

    /**
     * This method tells the handler to return the total price of the items in the user's cart
     * @return  the int total.
     */
    public int getTotal(){
        return this.cartHandler.getTotal();
    }

    /**
     * This method tells the handler to return if the user is logged in the system or not
     * @return the boolean which is returned by CartHandler
     */
    public boolean isLoggedIn(){
        return this.cartHandler.isLoggedIn();
    }

    /**
     * This method tells the handler to return the locally stored user cart
     * @return the cart which is returned by CartHandler
     */
    public ArrayList<Product> getCart(){
        return this.cartHandler.getCart();
    }
}

