package model;

import model.dataModels.Account;
import model.dataModels.Catalogue;
import model.dataModels.Product;

import java.io.*;
import java.util.ArrayList;

/**
 * Handler class for the locally stored catalogue and accounts in files and responsible for operations on it.
 */
public class FileHandler {
    private Account account;
    private Catalogue catalogue;
    private static boolean isLoggedIn;

    /**
     * Constructor that instantiates the locally stored catalogue and isLoggedIn boolean.
     */
    public FileHandler(){
        catalogue = new Catalogue();
        isLoggedIn = false;
    }

    /**
     * This method is used to create an account using the file system.
     * The method first loops through each line of the file and tries to find if
     * the username specified by the user already exists.
     * If it does not then it adds the username along with other credentials of the user
     * to the Accounts.txt file and logs the user in.
     * @param username - desired username of the user
     * @param password - desired password of the user
     * @param firstName - specified first name of the user
     * @param lastName - specified last name of the user
     */
    public void createAccount(String username, String password, String firstName, String lastName){
        boolean exists = false;
        try {
            BufferedReader buffer = new BufferedReader(new FileReader("Accounts.txt"));
            ArrayList<String> accountDetails = new ArrayList<>();
            String fetchedLine;
            String[] lineWords;
            while(true){
                fetchedLine = buffer.readLine();
                if(fetchedLine == null){
                    break;
                }else{
                    lineWords = fetchedLine.split("\t");

                    for (String word : lineWords) {
                        if (!"".equals(word)) {
                            accountDetails.add(word);
                        }
                        if (accountDetails.size() == 4) {
                            if (accountDetails.get(0).equals(username)) {
                                System.out.println("Account exists");
                                exists = true;
                                break;
                            }
                            accountDetails.clear();
                        }
                    }
                }
            }
            buffer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (!exists){
            try{
                File file = new File ("Accounts.txt");
                PrintWriter out = new PrintWriter(new FileWriter(file, true));
                out.append(username + "\t" + password + "\t" + firstName + "\t" + lastName+ "\n");
                out.close();
                logIn(username,password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is used to log the user in using the file system. The method uses an ArrayList to temporary hold
     * the values on each line then compares them with the specified values by the user. If they match, the isLoggedIn
     * boolean is set to true therefore the user is logged in
     * @param username - username with which the user wants to log in
     * @param password - password with which the user wants to log in.
     */
    public void logIn(String username, String password){
        try{
            BufferedReader buffer = new BufferedReader(new FileReader("Accounts.txt"));
            ArrayList<String> accountDetails = new ArrayList<>();
            String fetchedLine;
            String[] lineWords;
            while(true){
                fetchedLine = buffer.readLine();
                if(fetchedLine == null){
                    break;
                }else{
                    lineWords = fetchedLine.split("\t");

                    for (String word : lineWords) {
                        if (!"".equals(word)) {
                            accountDetails.add(word);
                        }
                        if (accountDetails.size() == 4) {
                            if (accountDetails.get(0).equals(username) && accountDetails.get(1).equals(password)) {
                                isLoggedIn = true;
                                account = new Account(accountDetails.get(2),accountDetails.get(3),accountDetails.get(0),
                                        accountDetails.get(1));
                                System.out.println("Logged In");
                                break;
                            } else {
                                accountDetails.clear();
                            }
                        }
                    }
                }
            }
            buffer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used to add items to the file system.
     * The method first goes through each line in Items.txt containing different items.
     * If the line does not contain the itemName and itemPrice specified by the user, the line is appended
     * to a string buffer. If the line does contain the itemName and itemPrice, the StringBuffer does not append
     * that particular line. At the end, StringBuffer appends the line along with the values given by the user
     * specified below. Using the StringBuffer, the FileWriter overwrites the file to give an updated list of items.
     * @param itemName - name of the item to be added to the system
     * @param itemQuantity - quantity of the item to be added to the system
     * @param itemPrice - price of the item to be added to the system
     */
    public void addToFileSystem(String itemName, String itemQuantity, String itemPrice){
        try{
            BufferedReader buffer = new BufferedReader(new FileReader("Items.txt"));
            StringBuffer sb=new StringBuffer("");
            ArrayList<String> itemDetails = new ArrayList<>();
            String fetchedLine;
            String[] lineWords;
            while(true){
                fetchedLine = buffer.readLine();
                if(fetchedLine == null){
                    break;
                }else{
                    lineWords = fetchedLine.split("\t");
                    for (String word : lineWords) {
                        if (!"".equals(word)) {
                            itemDetails.add(word);
                        }
                        if (itemDetails.size() == 3) {
                            if (!(itemDetails.get(0).equals(itemName) && itemDetails.get(2).equals(itemPrice))) {
                                sb.append(fetchedLine+"\n");
                            }
                            itemDetails.clear();
                        }
                    }
                }
            }
            buffer.close();
            sb.append(itemName + "\t" + itemQuantity + "\t" + itemPrice +"\n");

            FileWriter fw=new FileWriter(new File("Items.txt"));
            fw.write(sb.toString());
            fw.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method is used to add items to the local catalogue.
     * If they already exist on the catalogue, their quantity is increased based upon
     * the quantity specified.
     * @param itemName - name of the item to be added to the local catalogue
     * @param itemQuantity - quantity of the item to be added to the local catalogue
     * @param itemPrice - price of the item to be added to the local catalogue
     */
    public void addToLocalCatalog(String itemName, int itemQuantity, int itemPrice ){
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
     * This method is used to update the locally stored catalogue in the form of an ArrayList based upon
     * the items stored in the Items.txt file. This method is used initially and after a purchase.
     */
    public void updateCatalogue(){
        ArrayList<Product> products = new ArrayList<>();

        try{
            BufferedReader buffer = new BufferedReader(new FileReader("Items.txt"));
            ArrayList<String> itemDetails = new ArrayList<>();
            String fetchedLine;
            String[] lineWords;
            while(true){
                fetchedLine = buffer.readLine();
                if(fetchedLine == null){
                    break;
                }else{
                    lineWords = fetchedLine.split("\t");

                    for (String word : lineWords) {
                        if (!"".equals(word)) {
                            itemDetails.add(word);
                        }
                        if (itemDetails.size() == 3) {
                            Product p = new Product(itemDetails.get(0),Integer.parseInt(itemDetails.get(1)),
                                    Integer.parseInt(itemDetails.get(2)));
                            products.add(p);
                            itemDetails.clear();
                        }
                    }
                }
            }
            buffer.close();
            this.catalogue.setCatalogue(products);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This method is used to check if there exist a file called Accounts and Items, if either of them don't exist,
     * a file is created with the respective names
     */
    public void checkIsFile(){
        File f = new File("Accounts.txt");
        File f1 = new File("Items.txt");
        if (!f.isFile()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!f1.isFile()){
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used after each user operation of adding or removing items from or to the catalogue.
     * It is used to update the locally stored catalogue which is then used to update the Files after purchase.
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
     * This method is used to update the File System, specifically the Items.txt file according to the
     * locally stored catalogue.
     */
    public void updateFileSystem(){
        try
        {
            StringBuffer sb=new StringBuffer("");
            String line;
            for (int i = 0; i < getCatalogue().size();i++){
                Product temp = getCatalogue().get(i);
                line = temp.getName() + "\t" + temp.getQuantity() + "\t" + temp.getPrice();
                sb.append(line+"\n");
            }
            FileWriter fw=new FileWriter(new File("Items.txt"));
            fw.write(sb.toString());
            fw.close();
        }
        catch (Exception e)
        {
            System.out.println("Something went horribly wrong: "+e.getMessage());
        }
    }
    public boolean isLoggedIn(){
        return isLoggedIn;
    }
    public ArrayList<Product> getCatalogue(){
        return this.catalogue.getCatalogue();
    }
    public String getUserName(){
        return this.account.getUserName();
    }
}
