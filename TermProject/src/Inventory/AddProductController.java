package Inventory;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class AddProductController {

    /**
     * Declaration of a new product to be made.
     */
    Product newProduct;

    /**
     * Text fields and search field for the product's info to be input.
     */
    @FXML
    private TextField partSearchField;
    @FXML
    private TextField prodIdField;
    @FXML
    private TextField prodNameField;
    @FXML
    private TextField prodInvField;
    @FXML
    private TextField prodPriceField;
    @FXML
    private TextField prodMaxField;
    @FXML
    private TextField prodMinField;


    /**
     * Button and table components.
     */
    @FXML
    private Button btnCancel;
    @FXML
    public TableView<Part> availablePartTable;
    @FXML
    public TableColumn<Part, Integer> colAvailPartId;
    @FXML
    public TableColumn<Part, String> colAvailPartName;
    @FXML
    public TableColumn<Part, Integer> colAvailPartInv;
    @FXML
    public TableColumn<Part, Double> colAvailPartCost;
    @FXML
    public TableView<Part> prodPartTable;
    @FXML
    public TableColumn<Part, Integer> colAssocPartId;
    @FXML
    public TableColumn<Part, String> colAssocPartName;
    @FXML
    public TableColumn<Part, Integer> colAssocPartInv;
    @FXML
    public TableColumn<Part, Double> colAssocPartCost;

    /**
     * Add a part to the product's associated parts using the selected/highlighted item in the available parts table.
     */
    @FXML
    void prodAdd(ActionEvent event) {
        Part selectedItem = availablePartTable.getSelectionModel().getSelectedItem();
        newProduct.addAssociatedPart(selectedItem);
    }

    /**
     * Closes the window using the Cancel button's window/location.
     */
    @FXML
    void prodCancel(ActionEvent event) {
        //Closes Add Product window
        Stage productAddStage = (Stage) btnCancel.getScene().getWindow();
        productAddStage.close();
    }

    /**
     * Removes a part from the associated parts table for the current product being made.
     */
    @FXML
    void prodRemove(ActionEvent event) {
        Part selectedItem = prodPartTable.getSelectionModel().getSelectedItem();

        /**
         * First checks that a part is selected, and if not, displays a message telling the user to select one.
         * Uses JOptionPane for a Yes/No button-click answer to confirm whether or not a Part should be deleted. Runs deletePart method if it is a Yes.
         */
        if (selectedItem != null) {
            int confirmBtn = JOptionPane.YES_NO_OPTION;
            int resultBtn = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete this Part?", "Warning", confirmBtn);

            if (resultBtn == JOptionPane.YES_OPTION) {
                /**
                 * Deletes a selected part in the table. Returns a message if successful or an error message if a part was not selected.
                 */
                if (newProduct.deleteAssociatedPart(selectedItem)){
                    showMessageDialog(null, "Part successfully removed.");
                }
            }
        }
        /**
         * Only reaches this else-statement if no part is initially highlighted and selected.
         */
        else
            showMessageDialog(null, "Select a part to remove.");
    }

    @FXML
    void prodSave(ActionEvent event) {
        try {
            /**
             * Checks the logic between the minimum, maximum, and inventory fields before setting those values to the product, using the logic --
             * if (min <= max) AND (min >= 0) AND (inv >= min) AND (inv <= max)
             */
            if (Integer.parseInt(prodMinField.getText()) <= Integer.parseInt(prodMaxField.getText()) &&
                    Integer.parseInt(prodMinField.getText()) >= 0 &&
                    Integer.parseInt(prodInvField.getText()) >= Integer.parseInt(prodMinField.getText()) &&
                    Integer.parseInt(prodInvField.getText()) <= Integer.parseInt(prodMaxField.getText()) &&
                    Integer.parseInt(prodMaxField.getText()) >= 0) {

                /**
                 * Sets the product's parameters to the associated text field.
                 */
                newProduct.setName(prodNameField.getText());
                newProduct.setPrice(Double.parseDouble(prodPriceField.getText()));
                newProduct.setStock(Integer.parseInt(prodInvField.getText()));
                newProduct.setMin(Integer.parseInt(prodMinField.getText()));
                newProduct.setMax(Integer.parseInt(prodMaxField.getText()));

                /**
                 * Add 1 to the latest Id and set the new product Id to it for a contiguous unique ID.
                 */
                Inventory.incrementProdLatestId();
                newProduct.setId(Inventory.getProdLatestId());

                /**
                 * Add to Product ObservableList, shows up in Tableview.
                 */
                Inventory.addProduct(newProduct);

                /**
                 * Closes Add Product window.
                 */
                Stage productAddStage = (Stage) btnCancel.getScene().getWindow();
                productAddStage.close();
            }
            /**
             * Displays error messages for each logical error involving minimum, maximum, and inventory numbers.
             */
            else {
                if (Integer.parseInt(prodMinField.getText()) >= Integer.parseInt(prodMaxField.getText())) {
                    showMessageDialog(null, "Error: Minimum cannot be greater than Maximum.");
                }
                if (Integer.parseInt(prodMinField.getText()) < 0) {
                    showMessageDialog(null, "Error: Minimum cannot be less than zero.");
                }
                if (Integer.parseInt(prodInvField.getText()) < Integer.parseInt(prodMinField.getText())) {
                    showMessageDialog(null, "Error: Inventory cannot be less than Minimum.");
                }
                if (Integer.parseInt(prodInvField.getText()) > Integer.parseInt(prodMaxField.getText())) {
                    showMessageDialog(null, "Error: Inventory cannot be greater than Maximum.");
                }
                if (Integer.parseInt(prodMaxField.getText()) < 0) {
                    showMessageDialog(null, "Error: Maximum cannot be less than zero.");
                }
            }
        }
        /**
         * Catches the error when anything other than numerals are used in the text fields.
         */
                catch (NumberFormatException e) {
            showMessageDialog(null, "Invalid input. Please provide a number for Price, Minimum, Maximum, and Inventory.");
        }
    }

    @FXML
    /**
     * Actions done when this part of the program is loaded.
     */
    void initialize() {
        /**
         * Sets the columns of the available and associated parts tables to display what content belongs in that column.
         */
        newProduct = new Product(0,"",0,0,0,0);
        colAvailPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAvailPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAvailPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colAvailPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAssocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAssocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAssocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colAssocPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        availablePartTable.setPlaceholder(new Label("No parts were found."));
        prodPartTable.setPlaceholder(new Label("No parts are associated with this product."));
        /**
         * Creates a filter list using the allParts list and a listener to check for changes when searching for a part
         */
        FilteredList<Part> partFilteredList = new FilteredList<>(Inventory.getAllParts(), p -> true);
        partSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(Part -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (Integer.toString(Part.getId()).contains(partSearchField.getText())) {
                    return true;
                }
                else if (Part.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        /**
         * Creates a sorted list using the filtered list created
         */
        SortedList<Part> partSortedList = new SortedList<Part>(partFilteredList);
        partSortedList.comparatorProperty().bind(availablePartTable.comparatorProperty());

        /**
         * Sets the sorted searched parts list into the available parts table.
         */
        availablePartTable.setItems(partSortedList);
        prodPartTable.setItems(newProduct.getAllAssociatedParts());

        /**
         * Disables the editing of the product ID text field, as it is auto-generated.
         */
        prodIdField.setDisable(true);

    }
}
