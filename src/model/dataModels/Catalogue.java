package model.dataModels;

import java.util.ArrayList;

/**
 * Data model used by CatalogueHandler and FileHandler as a means to access
 */
public class Catalogue {
    private ArrayList<Product> catalogue;

    public Catalogue(){
        this.catalogue = new ArrayList<>();
    }
    public ArrayList<Product> getCatalogue(){
        return this.catalogue;
    }
    public void setCatalogue(ArrayList<Product> newCatalogue){
        this.catalogue = newCatalogue;
    }
}
