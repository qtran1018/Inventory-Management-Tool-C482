package Inventory;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * @author Quang Tran
 */

/**
 * Compatible Feature for future dev release: The program can be updated so that as products are created using parts, the parts available count decreases when it is input as a product component.
 * As of now, it was not required by the prompts, and so was not implemented.
 * A requirement for a prompt/message dialog to be interacted with and closed before being allowed to click more buttons on the main application screen would also be beneficial.
 */
public class Inventory extends Application implements Initializable {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /**
         * This method loads the FXML file for the main menu Inventory
         * and changes the window title.
         */
        Parent root = FXMLLoader.load(getClass().getResource("/Inventory/Inventory.fxml"));
        primaryStage.setTitle("Term Project Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public Product product;

    //<editor-fold desc="Declare FXML variables.">
    /**
     * This section sets up variables for all pieces of the menu,
     * Including any buttons, text fields, the 2 tables, and their columns.
     */
    @FXML
    private TextField partSearchField;
    @FXML
    private TextField prodSearchField;
    @FXML
    public TableView<Part> partTable;
    @FXML
    public TableColumn<Part, Integer> colPartId;
    @FXML
    public TableColumn<Part, String> colPartName;
    @FXML
    public TableColumn<Part, Integer> colPartInv;
    @FXML
    public TableColumn<Part, Double> colPartCost;
    @FXML
    public TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> colProdId;
    @FXML
    private TableColumn<Product, String> colProdName;
    @FXML
    private TableColumn<Product, Integer> colProdInv;
    @FXML
    private TableColumn<Product, Double> colProdCost;

    @FXML
    /**
     * Closes the program.
     */
    public void exitClick(ActionEvent event) {
        int confirmBtn = JOptionPane.YES_NO_OPTION;
        int resultBtn = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Warning", confirmBtn);

        if (resultBtn == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }
    //</editor-fold>

    //<editor-fold desc="FXML Action methods.">
    @FXML
    /**
     * Opens in a new window the form to add parts by accessing its FXML file.
     */
    public void partAddClick(ActionEvent event) throws Exception {
        FXMLLoader partLoader = new FXMLLoader(getClass().getResource("/Inventory/AddPart.fxml"));
        Parent partRoot = partLoader.load();
        Stage partStage = new Stage();
        partStage.setScene(new Scene(partRoot));
        partStage.show();
    }

    @FXML
    /**
     * Deletes a part.
     */
    public void partDeleteClick(ActionEvent event) {
        /**
         * Sets the part selectedItem to be the clicked/highlighted row of the Parts table.
         */
        Part selectedItem = partTable.getSelectionModel().getSelectedItem();

        /**
         * First checks that a part is selected, and if not, displays a message telling the user to select one.
         * Uses JOptionPane for a Yes/No button-click answer to confirm whether or not a Part should be deleted. Runs deletePart method if it is a Yes.
         */
        if (selectedItem != null) {
            int confirmBtn = JOptionPane.YES_NO_OPTION;
            int resultBtn = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete this Part?", "Warning", confirmBtn);

            if (resultBtn == JOptionPane.YES_OPTION) {
                if (deletePart(selectedItem)) {
                    showMessageDialog(null, "Part successfully deleted.");
                }
            }
        }
        /**
         * Only reaches this else-statement if no part is initially highlighted and selected.
         */
        else { showMessageDialog(null, "Select a part to delete."); }
    }

    @FXML
    /**
     * Modifies a part.
     */
    public void partModifyClick(ActionEvent event) throws Exception {
        try {
            /**
             * Sets the part selectedItem to be the clicked/highlighted row of the Parts table.
             */
            Part selectedItem = partTable.getSelectionModel().getSelectedItem();

            /**
             * Checks to see if an item is selected.
             */
            if (selectedItem != null) {

                FXMLLoader partLoader = new FXMLLoader(getClass().getResource("/Inventory/ModifyPart.fxml"));
                Parent partRoot = partLoader.load();
                Stage partStage = new Stage();
                partStage.setScene(new Scene(partRoot));

                /**
                 * Calls on a method in ModifyPartController to allow ModifyPartController to access the data of the selected part.
                 * This gets information of the selected part from InventoryController to ModifyPartController.
                 */
                ModifyPartController modControl = partLoader.getController();
                modControl.initPartData(selectedItem);

                /**
                 * Opens in a new window the form to add parts by accessing its FXML file loaded above.
                 */
                partStage.show();
            }
            /**
             * Prompts user to select an item if one isn't selected.
             */
            else {
                showMessageDialog(null, "Select a part to modify.");
            }
        }
        /**
         * Catches the error when no part is selected.
         */
        catch (Exception e) {
            //System.out.println(e.getMessage());
            showMessageDialog(null, "Select a part to modify.");
        }
    }

    @FXML
    /**
     * Add a product.
     */
    public void prodAddClick(ActionEvent event) throws Exception {
        /**
         * Opens in a new window the form to add products by accessing its FXML file.
         */
        FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/Inventory/AddProduct.fxml"));
        Parent productRoot = productLoader.load();
        Stage productStage = new Stage();
        productStage.setScene(new Scene(productRoot));
        productStage.show();

    }

    @FXML
    /**
     * Deletes a product.
     */
    public void prodDeleteClick(ActionEvent event) {

        /**
         * Assigns a Product variable to the item a user selected and clicked on.
         */
        Product selectedItem = productTable.getSelectionModel().getSelectedItem();

        /**
         * First checks that a product is selected, and if not, displays a message telling the user to select one.
         * Uses JOptionPane for a Yes/No button-click answer to confirm whether or not a Product should be deleted. Runs deleteProduct method if it is a Yes.
         */
        if (selectedItem != null && selectedItem.getAllAssociatedParts().isEmpty() == true) {
            int confirmBtn = JOptionPane.YES_NO_OPTION;
            int resultBtn = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete this Product?", "Warning", confirmBtn);
            if (resultBtn == JOptionPane.YES_OPTION) {
                if (deleteProduct(selectedItem)) {
                    showMessageDialog(null, "Product successfully deleted.");
                }
            }
        }
        /**
         * Catches the case in where a Product has parts still associated. Gives instruction to remove the associated parts before deleting the whole Product.
         */
        else if (selectedItem != null && selectedItem.getAllAssociatedParts().isEmpty() == false) {
            showMessageDialog(null, "Remove all associated parts on this product before deleting.");
        }
        /**
         * Catches the error when no product is selected.
         */
        else { showMessageDialog(null, "Select a product to Delete."); }
    }


    @FXML
    /**
     * Modifies a product.
     */
    public void prodModifyClick(ActionEvent event) throws Exception {
        try {
            /**
             * Sets the part selectedItem to be the clicked/highlighted row of the Product table.
             */
            Product selectedItem = productTable.getSelectionModel().getSelectedItem();

            /**
             * Checks to see if an item is selected.
             */
            if (selectedItem != null) {

                FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/Inventory/ModifyProduct.fxml"));
                Parent productRoot = productLoader.load();
                Stage productStage = new Stage();
                productStage.setScene(new Scene(productRoot));

                /**
                 * Calls on a method in ModifyProductController to allow ModifyProductController to access the data of the selected product.
                 * This gets information of the selected product from InventoryController to ModifyProductController.
                 */
                ModifyProductController modControl = productLoader.getController();
                modControl.initProductData(selectedItem);

                /**
                 * Opens in a new window the form to add parts by accessing its FXML file loaded above.
                 */
                productStage.show();
            }

            /**
             * Displays error message if no item is selected.
             */
            else
            {
                showMessageDialog(null, "Select a product to modify.");
            }
            /**
             * Catches the error when no product is selected.
             */
        }
        catch (Exception e) {
            showMessageDialog(null, "Select a product to modify.");
            //e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Initialize Method">
    @Override
    /**
     * Sets all the initial values for when the program first launches.
     */
    public void initialize(URL location, ResourceBundle resources) {
        colPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProdInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colProdCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setPlaceholder(new Label("No parts were found."));
        productTable.setPlaceholder(new Label("No products were found."));

        /**
         * Converts the parts observable list to a filtered list to utilise the search bar.
         * It takes in the parts table and a predicate, then determines whether or not the search value is within the table,
         * Adding the part to the displayed table as long as it matches, or the search bar is empty.
         */
        FilteredList<Part> partFilteredList = new FilteredList<>(allParts, p -> true);
        partSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(Part -> {
                /**
                 * Returns the whole parts table if the search field is blank.
                 */
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                /**
                 * Checks for a match via ID #.
                 */
                String lowerCaseFilter = newValue.toLowerCase();
                if (Integer.toString(Part.getId()).contains(partSearchField.getText())) {
                    return true;
                }
                /**
                 * Checks for a match via the name with all letters set to be lowercase.
                 */
                else if (Part.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        /**
         * Converts the product observable list to a filtered list to utilise the search bar.
         * It takes in the product table and a predicate, then determines whether or not the search value is within the table,
         * Adding the product to the displayed table as long as it matches, or the search bar is empty.
         */
        FilteredList<Product> prodFilteredList = new FilteredList<>(allProducts, p -> true);
        prodSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            prodFilteredList.setPredicate(Product -> {
                /**
                 * Returns the whole product table if the search field is blank.
                 */
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                /**
                 * Checks for a match via ID #.
                 */
                String lowerCaseFilter = newValue.toLowerCase();
                if (Integer.toString(Product.getId()).contains(prodSearchField.getText())) {
                    return true;
                }
                /**
                 * Checks for a match via the name with all letters set to be lowercase.
                 */
                else if (Product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        /**
         * Puts the newly filtered/searched list into a sorted list.
         */
        SortedList<Part> partSortedList = new SortedList<Part>(partFilteredList);
        partSortedList.comparatorProperty().bind(partTable.comparatorProperty());

        SortedList<Product> productSortedList = new SortedList<Product>(prodFilteredList);
        productSortedList.comparatorProperty().bind(productTable.comparatorProperty());


        /**
         * Puts the sorted lists into the tables displayed on the Inventory main page.
         */
        partTable.setItems(partSortedList);
        productTable.setItems(productSortedList);
    }
    //</editor-fold>


    private static ObservableList<Part> allParts = FXCollections.observableArrayList(
            /**
             * Sample data.
             */
            /*
            new InHousePart(1,"Sample Part 1",10,25,1,60,123),
            new InHousePart(2,"Sample Part 2",20,20,2,50,234),
            new InHousePart(3,"Sample Part 3",30,15,3,40,345),
            new InHousePart(4,"Sample Part 4",40,10,4,30,456),
            new InHousePart(5,"Sample Part 5",50,5,5,20,567),
            new InHousePart(6,"Sample Part 6",60,0,6,15,678)
             */
    );

    //<editor-fold desc="All Parts methods.">
    /**
     * @return allParts list.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * @param newPart the part to add to the parts table.
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * @param selectedPart the part selected to delete from the parts table.
     * @return a boolean on whether or not the part was deleted.
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    /**
     * @param index the location of the part in the table.
     * @param selectedPart the part to update into the table.
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * @param partId the int id value to search for in the parts table.
     * @return the part with a matching int id value.
     */
    public static Part lookupPart(int partId){
        for(Part var : allParts){
            if (var.getId() == partId){
                return var;
            }
        }
        return null;
    }

    /**
     * @param partName the string name value to search for in the parts table.
     * @return an observable list with all parts matching by String name value.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> lookupListPart = FXCollections.observableArrayList();
        for(Part var : allParts){
            if(var.getName().equals(partName)){
                lookupListPart.add(var);
            }
        }
        return lookupListPart;
    }
    //</editor-fold>

    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(
            /**
             * The list of all products, with sample data input.
             */
            new Product(1, "Sample Product 1", 10, 25, 1, 60),
            new Product(2, "Sample Product 2", 20, 20, 2, 50),
            new Product(3, "Sample Product 3", 30, 15, 3, 40),
            new Product(4, "Sample Product 4", 40, 10, 4, 30),
            new Product(5, "Sample Product 5", 50, 5, 5, 20),
            new Product(6, "Sample Product 6", 60, 0, 6, 15
            ));


    //<editor-fold desc="All Product methods.">
    /**
     * Uses the built-in functions of the tables to perform the necessary actions of
     * adding, deleting, getting, updating, and searching for products.
     * The 2 lookupProduct methods could have been used to search for matching products as the search field by creating a new observable list or
     * returning the observable list returned,
     * and setting that to the tableview via button clicks to search and reset.
     * @param newProduct the product to add to the product table.
     */
    public static void addProduct (Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * @param selectedProduct the selected product to delete.
     * @return a boolean if the product was successfully deleted or not.
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return the list of all products.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * @param index the location in the table of the product to modify
     * @param newProduct the new product (data) to update into the product table.
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * @param productId the int id value to search for in the product table.
     * @return the part with a matching int id value.
     */
    public static Product lookupProduct(int productId){
        for(Product var : allProducts){
            if (var.getId() == productId){
                return var;
            }
        }
        return null;
    }

    /**
     * @param productName the string name value to search for in the product table.
     * @return an observable list with all products matching by String name value.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> lookupListProduct = FXCollections.observableArrayList();
        for(Product var : allProducts){
            if (var.getName().equals(productName)){
                lookupListProduct.add(var);
            }
        }
        return lookupListProduct;
    }
    //</editor-fold>

    /**
     * Used to create unique Ids. Starts with list size, and only increments a variable to represent the latest ID of a part or product,
     */

    public static int partLatestId = allParts.size();
    public static Integer getPartLatestId() { return partLatestId; }
    public static void incrementPartLatestId(){ partLatestId++; }

    public static int prodLatestId = allProducts.size();
    public static Integer getProdLatestId() { return prodLatestId; }
    public static void incrementProdLatestId(){ prodLatestId++; }

    //<editor-fold desc="Main">
    /**
     * This is the main method that launches the program,
     * starting with the main menu Inventory screen for the program.
     * @param args for command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
    //</editor-fold>

}
