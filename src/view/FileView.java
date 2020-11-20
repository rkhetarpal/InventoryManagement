package view;
import controller.CartController;
import controller.FileController;
import model.dataModels.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This is the view for the File System.
 */
public class FileView {
    private FileController fileController;
    private CartController cartController;

    private JTable leftTable;           // Catalogue Table
    private JTable rightTable;          // Cart Table
    private JButton addButton;
    private JButton removeButton;
    private JButton logInButton;
    private JButton signUpButton;
    private JButton checkoutButton;
    private JButton resetButton;

    private JLabel totalLabel;          // Label that holds the total amount
    private JLabel userLabel;           // Label that holds the username
    private JLabel welcomeLabel;
    private JLabel systemLabel;         // Label that shows the type of system being used (File System for this case)

    private JFrame frame;

    private DefaultTableModel leftTableModel;
    private DefaultTableModel rightTableModel;
    private int total;

    /**
     * Constructor for the FileView that instantiates several different variables.
     */
    private FileView() {
        fileController = new FileController();
        cartController = new CartController();
        fileController.checkIsFile();
        fileController.updateCatalogue();
        total = 0;
        frame = new JFrame();
        frame.setTitle("ShopNet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setSize(1000, 500);

        setupTableModels();

        leftTable = new JTable(leftTableModel);
        rightTable = new JTable(rightTableModel);

        setupTable(leftTable);
        setupTable(rightTable);
        populateCatalogue();

        addButton = new JButton("Add >>");
        removeButton = new JButton("<< Remove");

        totalLabel = new JLabel("Total amount : " + total);

        systemLabel = new JLabel("File System");

        logInButton= new JButton("Login");

        signUpButton = new JButton("SignUp");

        checkoutButton = new JButton("Checkout");
        checkoutButton.setBackground(Color.GREEN);
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("Dialog", Font.PLAIN, 15));
        checkoutButton.setPreferredSize(new Dimension(120, 40));

        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(70,20));

        userLabel = new JLabel("User: Not Logged In");
        Font f1 = new Font("Dialog", Font.PLAIN, 20);
        userLabel.setFont(f1);
        userLabel.setForeground(Color.RED);

        welcomeLabel = new JLabel("Welcome to ShopNet");
        Font f = new Font("Dialog", Font.PLAIN, 20);
        welcomeLabel.setFont(f);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;          // Components start being added from the top left.

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(60,-30,40,-30);
        panel.add(signUpButton, gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(60,-30,40,-50);
        panel.add(logInButton, gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(systemLabel,gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;           // Fills the component horizontally.
        panel.add(addButton, gbc);

        gbc.insets = new Insets(40,0,0,0);
        panel.add(removeButton, gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,40);
        panel.add(welcomeLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30,0,0,40);
        panel.add(userLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(checkoutButton,gbc);

        gbc.insets = new Insets(50,0,0,0);
        panel.add(totalLabel,gbc);
        gbc.insets = new Insets(0,0,0,0);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(0,200,20,0);
        panel.add(resetButton,gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.33;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        frame.add(new JLabel("Catalogue"), gbc);
        gbc.gridx++;
        frame.add(new JPanel(), gbc);
        gbc.gridx++;
        frame.add(new JLabel("Your Cart"), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weighty++;
        gbc.fill = GridBagConstraints.BOTH;

        frame.add(new JScrollPane(leftTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,       // Adds left table to the
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), gbc);                          // frame along with scroll
        gbc.gridx++;                                                                        // bar if needed
        frame.add(panel, gbc);
        gbc.gridx++;
        frame.add(new JScrollPane(rightTable), gbc);

        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        frame.setVisible(true);

        /*
         * Used to enable or disable the add button based upon leftTable row being selected or not.
         */
        leftTable.getSelectionModel().addListSelectionListener(e -> {
            int count = leftTable.getSelectedRowCount();
            addButton.setEnabled(count > 0);

        });

        /*
         * Used to enable or disable the remove button based upon leftTable row being selected or not.
         */
        rightTable.getSelectionModel().addListSelectionListener(e -> {
            int count = rightTable.getSelectedRowCount();
            removeButton.setEnabled(count > 0);
        });

        /*
         * Resets the cart of the user and adds the cart items back to the catalogue
         */
        resetButton.addActionListener(e -> {
            for (int i = 0; i < cartController.getCart().size(); i++){
               Product p = cartController.getCart().get(i);
                fileController.addToLocalCatalogue(p.getName(),p.getQuantity(),p.getPrice());
            }
            cartController.clearContents();
            populateCart();
            populateCatalogue();
            total = 0;
            totalLabel.setText("Total amount : " + total);
        });

        /*
         * Button used to mock payments being made.
         */
        checkoutButton.addActionListener(e -> {
            if (!fileController.isLoggedIn()){
                displayErrorMessage("Please Log in First", "Log in");
            }
            else {
                int n = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you got all that you need?",
                        "Checkout",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION ){
                    if(cartController.getCart().isEmpty()){
                        displayErrorMessage("Your Cart is Empty", "Checkout");
                    }
                    else {
                        displaySuccessMessage("Checkout done. Your account was credited with "
                                + cartController.getTotal() + " amount", "Checkout");
                        fileController.checkout();
                        cartController.clearContents();
                        populateCart();
                    }
                }
            }
        });

        /*
         * This button is used to sign up a user. It opens an information field in which user inputs
         * different credentials and logs in.
         */
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(0, 1));
                JTextField field1 = new JTextField("");
                JPasswordField field2 = new JPasswordField("");
                JTextField field3 = new JTextField("");
                JTextField field4 = new JTextField("");
                panel.add(new JLabel("Username:"));
                panel.add(field1);
                panel.add(new JLabel("Password"));
                panel.add(field2);
                panel.add(new JLabel("First Name"));
                panel.add(field3);
                panel.add(new JLabel("Last Name"));
                panel.add(field4);

                int result = JOptionPane.showConfirmDialog(null,panel,"SignUp",
                        JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String passText = new String (field2.getPassword());
                    if (field1.getText().isEmpty() || passText.isEmpty() || field3.getText().isEmpty() ||
                            field4.getText().isEmpty()){
                        displayErrorMessage("Please fill in all details", "SignUp");
                        actionPerformed(e);
                    }
                    else {
                        fileController.createAccount(field1.getText().replaceAll("\\s+",""),
                                passText.replaceAll("\\s+",""),field3.getText().replaceAll("\\s+",""),
                                field4.getText().replaceAll("\\s+",""));
                        if(fileController.isLoggedIn()){
                            displaySuccessMessage("Logged in successfully","Login");
                            userLabel.setText("Logged in: " + fileController.getUserName());
                            userLabel.setForeground(Color.GREEN);
                        }
                        else {
                            displayErrorMessage("SignUp unsuccessful: Account already exists", "SignUp");
                        }
                    }
                }
                else {
                    System.out.println("Cancelled");
                }

            }
        });

        /*
         * Button used to log the user in using the JDialog Box.
         */
        logInButton.addActionListener(e -> {
            JPanel panel1 = new JPanel(new GridLayout(0, 1));
            JTextField field1 = new JTextField("");
            JPasswordField field2 = new JPasswordField("");
            panel1.add(new JLabel("Username:"));
            panel1.add(field1);
            panel1.add(new JLabel("Password"));
            panel1.add(field2);

            int result = JOptionPane.showConfirmDialog(null, panel1,"Login",
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String passText = new String (field2.getPassword());
                System.out.println("Username " + field1.getText());
                System.out.println("Password " + passText);
                fileController.logIn(field1.getText().replaceAll("\\s+",""),passText.replaceAll("\\s+",""));
                if(fileController.isLoggedIn()){
                    displaySuccessMessage("Logged in successfully","Login");
                    userLabel.setText("Logged in: " + fileController.getUserName());
                    userLabel.setForeground(Color.GREEN);
                }
                else {
                    displayErrorMessage("Log in unsuccessful", "Login");
                }
            } else {
                System.out.println("Cancelled");
            }
        });

        /*
         * Button used to add items from the catalogue to the cart.
         * This method reduces quantity from the catalogue and increases/adds quantity to the cart.
         */
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileController.isLoggedIn()){
                    displayErrorMessage("Please Log in First", "Log in");
                }
                else {
                    String itemName =  (String) leftTable.getModel().getValueAt(leftTable.getSelectedRow(),0);
                    int itemQuantity = (int) leftTable.getModel().getValueAt(leftTable.getSelectedRow(), 1);
                    int itemPrice = (int) leftTable.getModel().getValueAt(leftTable.getSelectedRow(), 2);

                    String input = JOptionPane.showInputDialog(frame, "How much quantity of " + itemName
                            + " below " + itemQuantity + " would you like?");
                    if (!input.isEmpty() && !containsOnlyNumbers(input)){
                        displayErrorMessage("Please enter a valid quantity", "Add");
                        actionPerformed(e);
                    }
                    else if (Integer.parseInt(input) > itemQuantity) {
                        displayErrorMessage("Please enter a quantity below " + itemQuantity, "Add");
                        actionPerformed(e);
                    }
                    else {
                        cartController.addItem(itemName,Integer.parseInt(input),itemPrice);
                        populateCart();
                        total+= (Integer.parseInt(input) * itemPrice);
                        totalLabel.setText("Total amount: " + total);
                        fileController.updateLocalCatalogue(itemName, itemQuantity - Integer.parseInt(input),
                                itemPrice);
                        populateCatalogue();
                    }
                }
            }
        });

        /*
         * This button is used to remove items from the cart one by one to the catalogue.
         * The item is completely removed from the cart and is updated in the catalogue.
         */
        removeButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Are you sure you want to remove " +
                            "that item?", "Remove Item?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // yes option
                String itemName =  (String) rightTable.getModel().getValueAt(rightTable.getSelectedRow(),0);
                int itemQuantity = (int) rightTable.getModel().getValueAt(rightTable.getSelectedRow(),1);
                int itemPrice = (int) rightTable.getModel().getValueAt(rightTable.getSelectedRow(), 2);
                cartController.removeItem(itemName,itemPrice);
                fileController.addToLocalCatalogue(itemName,itemQuantity,itemPrice);
                total -= (itemQuantity * itemPrice);
                totalLabel.setText("Total amount : " + total);
                populateCart();
                populateCatalogue();

            }
        });
    }

    /**
     * This method is used to populate the table with the locally stored cart.
     */
    private void populateCart(){
        if (rightTableModel.getRowCount() > 0) {
            for (int i = rightTableModel.getRowCount() - 1; i > -1; i--) {
                rightTableModel.removeRow(i);
            }
        }
        ArrayList<Product> pro = cartController.getCart();
        Object rowData[] = new Object[3];
        for (Product aPro : pro) {
            rowData[0] = aPro.name;
            rowData[1] = aPro.quantity;
            rowData[2] = aPro.price;
            rightTableModel.addRow(rowData);
        }
    }

    /**
     * This method is used to make the tables un-editable.
     */
    private void setupTableModels(){
        leftTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rightTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * This method is used to populate the table with the locally stored catalogue.
     */
    private void populateCatalogue() {
        // DefaultTableModel dm = (DefaultTableModel)leftTable.getModel();
        if (leftTableModel.getRowCount() > 0) {
            for (int i = leftTableModel.getRowCount() - 1; i > -1; i--) {
                leftTableModel.removeRow(i);
            }
        }
        ArrayList<Product> pro = fileController.getCatalogue();
        Object rowData[] = new Object[3];
        for (Product aPro : pro) {
            rowData[0] = aPro.name;
            rowData[1] = aPro.quantity;
            rowData[2] = aPro.price;
            leftTableModel.addRow(rowData);
        }
    }

    /**
     * Method used to display success messages. The message shown and the title of the JDialogBox
     * is used from the parameters.
     * @param message - message that user wants to display on the JDialogBox
     * @param title - title of the JDialogBox that the user wants to display
     */
    private void displaySuccessMessage(String message, String title){
        JOptionPane.showMessageDialog(this.frame,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * * Method used to display error messages. The message shown and the title of the JDialogBox
     * is used from the parameters.
     * @param message - message that the user wants to display on the JDialogBox
     * @param title - title of the JDialogBox that the user wants to display
     */
    private void displayErrorMessage(String message, String title){
        JOptionPane.showMessageDialog(this.frame,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method used to ensure that the String is made up of just numbers. Used when JTextFields are created
     * which are meant to only have numbers in them.
     * @param str - String used to check if it contains numbers or not
     * @return - return statement that returns true if str contains only numbers of false instead
     */
    private boolean containsOnlyNumbers(String str){
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
    /**
     * This method is used to setup the table models of the tables. Height adjustments, cell size adjustment,
     * font adjustments etc. sorts of modifications to the table are done in this method.
     * @param table - the TableModel to be worked upon is extracted from this table itself
     */
    private void setupTable(JTable table) {
        DefaultTableModel dm = (DefaultTableModel) table.getModel();
        dm.addColumn("Name");
        dm.addColumn("Quantity");
        dm.addColumn("Price");
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont((new Font("Serif", Font.BOLD, 12)));
        table.setRowHeight(50);
        DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
        centreRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        table.getColumn("Name").setCellRenderer( centreRenderer );
        table.getColumn("Price").setCellRenderer( centreRenderer );
        table.getColumn("Quantity").setCellRenderer( centreRenderer );
        table.getColumnModel().getColumn(0).setMinWidth(150);

    }

    /**
     * Invokes the FileView() constructor
     * @param args
     */
    public static void main (String [] args){
        FileView fv = new FileView();
    }
}
