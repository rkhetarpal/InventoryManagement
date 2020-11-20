package controller;

import model.FileHandler;
import model.dataModels.Product;

import java.util.ArrayList;

/**
 * The controller part of the File System which acts as the medium between the View and the Model
 * of the file.
 */
public class FileController {
    private FileHandler fileHandler;

    /**
     * Instantiates the FileHandler class
     */
    public FileController(){
        this.fileHandler = new FileHandler();
    }


    public void createAccount(String username, String password, String firstName, String lastName){
        this.fileHandler.createAccount(username,password,firstName,lastName);
    }
    public String getUserName(){
        return this.fileHandler.getUserName();
    }
    public void logIn(String username, String password){
        this.fileHandler.logIn(username,password);
    }
    public void updateLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
        this.fileHandler.updateLocalCatalogue(itemName,itemQuantity,itemPrice);
    }
    public void updateCatalogue(){
        this.fileHandler.updateCatalogue();
    }
    public void addToLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
        this.fileHandler.addToLocalCatalog(itemName,itemQuantity,itemPrice);
    }
    public ArrayList<Product> getCatalogue() {
        return this.fileHandler.getCatalogue();
    }

    public boolean isLoggedIn(){
        return this.fileHandler.isLoggedIn();
    }
    public void checkout(){
        this.fileHandler.updateFileSystem();
    }
    public void checkIsFile(){
        this.fileHandler.checkIsFile();
    }
}
