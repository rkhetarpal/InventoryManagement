package controller;

import model.CatalogueHandler;
import model.dataModels.Product;

import java.util.ArrayList;

/**
 * The controller part of the catalogue which acts as the medium between the View and the Model
 * of the catalogue kept on a database.
 */
public class CatalogueController {
    private CatalogueHandler catalogueHandler;

    public CatalogueController(){
        this.catalogueHandler = new CatalogueHandler();
    }

    public void updateCatalogue(){
       this.catalogueHandler.updateCatalogue();
     }
     public void logIn(String userName, String password){
         this.catalogueHandler.logIn(userName,password);
     }

     public ArrayList<Product> getCatalogue(){
         return this.catalogueHandler.getCatalogue();
     }
     public String getUserName(){
         return this.catalogueHandler.getUserName();
     }
     public void createAccount(String username, String password, String firstName, String lastName){
         this.catalogueHandler.createAccount(username,password,firstName,lastName);
     }

     public void updateLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
         this.catalogueHandler.updateLocalCatalogue(itemName, itemQuantity, itemPrice);
     }

     public void addToLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
         this.catalogueHandler.addToLocalCatalogue(itemName,itemQuantity,itemPrice);
     }
     public void checkSchemas(){
         this.catalogueHandler.checkSchemas();
     }
     public void createDatabase(){
         this.catalogueHandler.createDatabase();
     }

    /**
     * Tells the handler to update the database after a user has finished purchasing items.
     */
    public void checkout(){
         this.catalogueHandler.updateDB(catalogueHandler.getCatalogue());
     }
}
