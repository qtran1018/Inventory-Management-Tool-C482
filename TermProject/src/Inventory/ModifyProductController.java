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

public class ModifyProductController {

    public Product product;

    @FXML
    private TextField partSearchField;

    @FXML
    private TextField prodIDField;

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

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    /**
     * Uses the selected highlighted part via the TableView's methods to initialize a part variable and adds it to the product's part table.
     */
    void prodAdd(ActionEvent event) {
        Part selectedItem = availablePartTable.getSelectionModel().getSelectedItem();
        product.addAssociatedPart(selectedItem);
    }

    @FXML
    /**
     * Closes the modify product window using the cancel button's location/window as reference.
     */
    void prodCancel(ActionEvent event) {
        //Closes Add Product window
        Stage productAddStage = (Stage) btnCancel.getScene().getWindow();
        productAddStage.close();
    }

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
                if (product.deleteAssociatedPart(selectedItem)){
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

    /**
     * GRADING SECTION 3:
     * Error Description and Fix: Normally, tables were set to initialize in the initialize() method at the bottom of this code. However, the issue of nullpointerexception came up,
     * where upon initialization, there would be no observable list to set into the tableview. This was solved by instead moving where and thus when the tableview would look for a list to set. By moving it here to the initProductData method,
     * this ensured that the program would only be set AFTER initProductData was run (which copies the selected product in InventoryController over to this Java class ModifyProductController).
     * Used to access and replicate data of a selected part to modify in the Inventory main menu.
     * @param product is the selected product in InventoryController's product table.
     */
    public void initProductData(Product product) {
        try {
            /**
             * Sets the text of text fields when the product is opened to be modified.
             */
            this.product = product;
            prodIDField.setText(Integer.toString(product.getId()));
            prodNameField.setText(product.getName());
            prodPriceField.setText(Double.toString(product.getPrice()));
            prodInvField.setText(Integer.toString(product.getStock()));
            prodMinField.setText(Integer.toString(product.getMin()));
            prodMaxField.setText(Integer.toString(product.getMax()));
            prodPartTable.setItems(product.getAllAssociatedParts()); //TODO: CHECKLIST G-3, NULLPOINTEREXCEPTION, LOADED THIS TABLE ONLY WHEN PRODUCT WAS INITIALIZED
        }
        catch (NullPointerException e) {
            /**
             * Catches any exceptions, and is handled in InventoryController, if no product is selected.
             */
        }
    }

    @FXML
    void prodSave(ActionEvent event) {
        int indexProduct = Inventory.getAllProducts().indexOf(product);

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
                 * Sets all applicable fields except for Id, which is unchangeable.
                 */
                product.setName(prodNameField.getText());
                product.setPrice(Double.parseDouble(prodPriceField.getText()));
                product.setStock(Integer.parseInt(prodInvField.getText()));
                product.setMax(Integer.parseInt(prodMaxField.getText()));

                /**
                 * Updates the product via InventoryController's updateProduct method, using the product initialized in the initProductData method above.
                 */
                Inventory.updateProduct(indexProduct, product);

                /**
                 * Closes the window via the save button's current location and window.
                 */
                Stage partAddStage = (Stage) btnSave.getScene().getWindow();
                partAddStage.close();
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
    void initialize() {
        /**
         * Sets the columns of the available and associated parts tables to display what content belongs in that column.
         */
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
        /**
         * Disables the editing of the product ID text field.
         */
        prodIDField.setDisable(true);
    }
}
