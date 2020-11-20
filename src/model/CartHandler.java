package model;

import model.dataModels.Cart;
import model.dataModels.Product;

import java.util.ArrayList;

/**
 * Handler class for the locally stored user cart which performs all operations related to the user cart
 */
public class CartHandler {
    private Cart cart;

    /**
     * Constructor that creates a new user cart and sets isLoggedIn false.
     */
    public CartHandler(){
        this.cart = new Cart();
    }

    /**
     * Method used to add item to the cart. If the item name along with the price already exists in the cart,
     * the itemQuantity variable is added to the item's quantity to increase it. Otherwise the product is simply
     * added to the cart (ArrayList),
     * @param itemName - name of the item to be added to the cart
     * @param itemQuantity - quantity of the item to be added to the cart
     * @param itemPrice - price of the item to be added to the cart
     */
    public void addItem(String itemName, int itemQuantity, int itemPrice){
        boolean exists = false;
        if (!this.cart.getCart().isEmpty()){
            for (Product aProduct : this.cart.getCart()) {
                if (aProduct.getName().equals(itemName)){
                    aProduct.setQuantity(aProduct.getQuantity() + itemQuantity);
                    exists = true;
                }
            }
        }
        if (!exists){
            Product P = new Product(itemName,itemQuantity,itemPrice);
            this.cart.getCart().add(P);
        }
    }

    /**
     * Method used to remove an item from the cart. This method does not have a check method to ensure that the item
     * is in the user cart as that is a given considering how the view does not let this method to be invoked unless
     * there is an item present to be removed from the user cart
     * @param itemName - name of the item to be removed from the cart
     * @param itemPrice - price of the item to be removed from the cart
     */
    public void removeItem(String itemName, int itemPrice){
        for (Product aProduct : this.cart.getCart()) {
            if (aProduct.getName().equals(itemName) && aProduct.getPrice() == itemPrice){
                this.cart.getCart().remove(aProduct);
                break;
            }
        }
    }
    public void clearContents(){
        this.cart.getCart().clear();
    }

    /**
     * This method returns the cumulative total of the user cart's item prices.
     * @return
     */
    public int getTotal(){
        int total = 0;
        for (Product aProduct : this.cart.getCart()) {
            total += aProduct.getPrice() * aProduct.getQuantity();
        }
        return total;
    }
    public boolean isLoggedIn(){
        return CatalogueHandler.loggedIn;
    }
    public ArrayList<Product> getCart(){
        return this.cart.getCart();
    }
}
