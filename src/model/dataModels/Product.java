package model.dataModels;

/**
 * Data model used specifically by the Cart and Catalogue class as a means of Object Arrays used to store
 * product details as mentioned below
 */
public class Product {
    public int quantity;
    public int price;
    public String name;

    /**
     * Constructor for creating a product using the parameters given below.
     * @param name - name of the item
     * @param quantity - quantity of the item
     * @param price - price of the item
     */
    public Product(String name,int quantity, int price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getName(){
        return this.name;
    }
    public int getPrice(){
        return this.price;
    }
}
