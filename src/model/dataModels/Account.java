package model.dataModels;

/**
 * Data Model that is used by the CatalogueHandler and FileHandler class as a means
 * to have access to the attributes of the logged in account.
 */
public class Account {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    /**
     * Constructor for creating an account.
     * @param firstName - first name of the user to which the account belongs to
     * @param lastName - last name of the user to which the account belongs to
     * @param userName - username of the user to which the account belongs to
     * @param password - password of the user to which the account belongs to
     */
    public Account(String firstName, String lastName, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
}
