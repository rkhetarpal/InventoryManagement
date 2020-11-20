package model;

import model.dataModels.Account;
import model.dataModels.Catalogue;
import model.dataModels.Product;

import java.sql.*;
import java.util.ArrayList;

/**
 * Handler class for the locally stored catalogue as well as the class responsible for DB connections and operations.
 */
public class CatalogueHandler {
    Catalogue catalogue;
    Account account;
    private Connection c;          // Used for connection to the local SQLite Database
    private Statement stmt;         // Used to create query statements.
    static boolean loggedIn;

    /**
     * Constructor that instantiates some variables required in the class.
     */
    public CatalogueHandler(){
        catalogue = new Catalogue();
        c = null;
        stmt = null;
        loggedIn = false;
    }

    /**
     * This method uses the parameter to query the database whether the username and password exist on it or not
     * if they do, isLoggedIn is set to true thus allowing user to carry out purchases.
     * @param username - username with which the user wants to log in
     * @param password - password with which the user wants to log in
     */
    public void logIn(String username, String password){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");         // ShoppingCart.db is the name
            c.setAutoCommit(false);                                                     // of the local DB created
            System.out.println("Opened database successfully");                         // further down

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ACCOUNTS WHERE user_name" +
                    " = '" + username + "' AND password = '" + password + "';" );
            if (!rs.next()) {
                System.out.println("Incorrect credentials or Account does not exist");
            }
            else {

                /*
                 * The account is instantiated here since there exists a userName and password on the database
                 * as specified by the user.
                 */
                account = new Account(rs.getString("first_name"),rs.getString("last_name"),
                        rs.getString("user_name"),rs.getString("password"));
                String userName = rs.getString("user_name");
                System.out.println("User Name: " + userName);
                loggedIn = true;
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method is used to create an account on the database.
     * The method queries database to find the username specified by the user,
     * if it already exists then it asks the user to input different credentials.
     * If the username does not already exist, then it makes the account and logs it in.
     * @param username - desired username of the user
     * @param password - desired password of the user
     * @param firstName - specified first name of the user
     * @param lastName - specified last name of the user
     */
    public void createAccount(String username, String password,String firstName, String lastName){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String rest = "SELECT * FROM ACCOUNTS WHERE user_name" +
                    " = '" + username + "';";
            System.out.println(rest);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ACCOUNTS WHERE user_name" +
                    " = '" + username + "';" );
            if (!rs.next()) {
                stmt = c.createStatement();
                String sql = "INSERT INTO ACCOUNTS (user_name,password,first_name,last_name) " +
                        "VALUES ('" +  username + "','" + password + "','" +  firstName +
                        "','" + lastName + "');";
                stmt.executeUpdate(sql);
                System.out.println(sql);
            }
            else {
                System.out.println("Account already exists with the username: " + username);
            }
            stmt.close();
            c.commit();
            c.close();
            rs.close();
            logIn(username,password);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method updates attributes of a product on the database.
     * @param itemName - name of the item that needs to be updated
     * @param itemQuantity - possibly new quantity of the item that needs to be updated
     * @param itemPrice - possibly new price of the item that needs to be updated
     */
    public void changeItemTo(String itemName, int itemQuantity,int itemPrice){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "UPDATE ITEMS set quantity = " + itemQuantity + ", price = " + itemPrice +
                    " where name = '" + itemName + "';";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            updateCatalogue();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method is called after a user makes a purchase and it uses the locally saved catalogue to update
     * the database to ensure data integrity.
     * @param catalogue - the locally catalogue used to update the database with
     */
    public void updateDB(ArrayList<Product> catalogue){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            for (Product aCatalogue : catalogue) {
                String itemName = aCatalogue.getName();
                int itemQuantity = aCatalogue.getQuantity();
                int itemPrice = aCatalogue.getPrice();
                stmt = c.createStatement();
                String sql = "UPDATE ITEMS set quantity = " + itemQuantity + ", price = " + itemPrice +
                        " where name = '" + itemName + "';";
                stmt.executeUpdate(sql);
                c.commit();
            }
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method is used to update the locally stored catalogue upon changes made by the user to add something
     * from the catalogue to the cart or by removing something from the cart to the catalogue.
     * @param itemName - name of the item to be updated to the local catalogue
     * @param itemQuantity - quantity of the item to be updated to the local catalogue
     * @param itemPrice - price of the item to be updated to the local catalogue
     */
    public void updateLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
            for (Product aProduct : this.catalogue.getCatalogue()) {
                if (aProduct.getName().equals(itemName) && aProduct.getPrice() == itemPrice){
                    aProduct.setQuantity(itemQuantity);
                }
            }
        }

    /**
     * This method is used to update the locally stored catalogue at initialization. It queries the Items table
     * and stores the result in the Catalogue object.
     */
    public void updateCatalogue(){
        ArrayList<Product> products = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ITEMS;" );
            while (rs.next()) {
                int itemQuantity = rs.getInt("quantity");
                String itemName = rs.getString("name");
                int itemPrice  = rs.getInt("price");
                System.out.println( "Quantity  = " + itemQuantity );
                System.out.println( "NAME = " + itemName );
                System.out.println( "Price = " + itemPrice );
                System.out.println();

                Product p = new Product(itemName,itemQuantity,itemPrice);
                products.add(p);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        catalogue.setCatalogue(products);
    }

    /**
     * This method is used to create a database if it does not exist. It does not overwrite already created Databases.
     */
    public void createDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            System.out.println("Opened database successfully");

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }

    /**
     * This method is used to check whether there already exists schemas on the database, if they don't then
     * createSchemas method is called.
     */
    public void checkSchemas(){
        try {
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            DatabaseMetaData md = c.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            if (!rs.next()){
                System.out.println("No tables exist");
                this.createSchemas();
            }
            else {
                System.out.println("Tables exist already");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates schemas on the database ShoppingCart.db.
     * It creates the ITEMS and ACCOUNTS schema.
     */
    private void createSchemas(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE ACCOUNTS " +
                    "(user_name TEXT PRIMARY KEY     NOT NULL," +
                    " password           TEXT    NOT NULL, " +
                    " first_name           TEXT     NOT NULL, " +
                    " last_name        TEXT      NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "CREATE TABLE ITEMS " +
                    "(name         TEXT       NOT NULL," +
                    " quantity     INTEGER    NOT NULL, " +
                    " price        INTEGER    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    /**
     * This method can be used to add an item to the catalogue database by the owners of ShopNet(this application).
     * @param itemName - name of the item to be added to the database
     * @param itemQuantity - quantity of the item to be added to the database
     * @param itemPrice - quantity of the item to be added to the database
     */
    public void addItem(String itemName, int itemQuantity, int itemPrice){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO ITEMS (name,quantity,price) " +
                    "VALUES ('" + itemName + "'," + itemQuantity + "," + itemPrice + ");";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            updateCatalogue();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method can be used to remove an item to the catalogue database by the owners of ShopNet(this application).
     * @param itemName - name of the item to be removed from the database
     * @param itemPrice - price of the item to be removed from the database
     */
    public void removeItem(String itemName, int itemPrice){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ShoppingCart.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "DELETE from ITEMS where name = '" + itemName + "' AND price = " + itemPrice + " ;";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
            updateCatalogue();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }

    /**
     * This method is used to add new items to the locally stored catalogue.
     * @param itemName - name of the item to be added to the local catalogue
     * @param itemQuantity - quantity of the item to be added to the local catalogue
     * @param itemPrice - price of the item to be added to the local catalogue
     */
    public void addToLocalCatalogue(String itemName, int itemQuantity, int itemPrice){
        boolean exists = false;
        if (!this.getCatalogue().isEmpty()){
            for (Product aProduct : this.getCatalogue()) {
                if (aProduct.getName().equals(itemName)){
                    aProduct.setQuantity(aProduct.getQuantity() + itemQuantity);
                    exists = true;
                }
            }
        }
        if (!exists){
            Product P = new Product(itemName,itemQuantity,itemPrice);
            this.getCatalogue().add(P);
        }
    }
    /**
     * This method returns the user name of the logged in account.
     * @return returns the username of the account with which the user has logged in
     */
    public String getUserName(){
        return this.account.getUserName();
    }
    public ArrayList<Product> getCatalogue(){
        return this.catalogue.getCatalogue();
    }

}
