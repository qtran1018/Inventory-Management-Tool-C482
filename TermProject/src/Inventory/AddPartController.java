package Inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static javax.swing.JOptionPane.showMessageDialog;

public class AddPartController {

    /**
     * Text fields and search field for the part's info to be input.
     */
    @FXML
    private TextField partIDField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partInvField;
    @FXML
    private TextField partPriceField;
    @FXML
    private TextField partMaxField;
    @FXML
    private TextField partMinField;
    @FXML
    private Label partMachineIdLabel;
    @FXML
    private TextField partMachineIdField;
    @FXML
    private Label partCompanyNameLabel;
    @FXML
    private TextField partCompanyNameField;
    @FXML
    private RadioButton btnInHouse;
    @FXML
    private RadioButton btnOutsourced;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    /**
     * A radio button to select what type of part is created, inhouse or outsourced,
     * with the difference being either having a company name or machine id.
     */
    @FXML
    void inHouse(ActionEvent event) {

        //Sets in-house machine vars as selected, deselects company-name vars
        btnOutsourced.setSelected(false);
        partCompanyNameLabel.setVisible(false);
        partCompanyNameField.setVisible(false);
        partMachineIdLabel.setVisible(true);
        partMachineIdField.setVisible(true);
    }

    /**
     * A radio button to select what type of part is created, inhouse or outsourced,
     * with the difference being either having a company name or machine id.
     */
    @FXML
    void outSourced(ActionEvent event) {
        //Sets in-house company vars as selected, deselects machine vars
        btnInHouse.setSelected(false);
        partMachineIdLabel.setVisible(false);
        partMachineIdField.setVisible(false);
        partCompanyNameLabel.setVisible(true);
        partCompanyNameField.setVisible(true);
    }

    /**
     * Closes the window using the Cancel button's window/location.
     */
    @FXML
    void partCancel(ActionEvent event) {
        Stage partAddStage = (Stage) btnCancel.getScene().getWindow();
        //Closes Add Part window
        partAddStage.close();
    }

    /**
     * Saves the created part with the inputted fields to the allParts list in InventoryController.
     */
    @FXML
    void partSave(ActionEvent event) {
        try {
            if (btnInHouse.isSelected() || btnOutsourced.isSelected()){
                /**
                 * Checks the logic between the minimum, maximum, and inventory fields before setting those values to the product, using the logic --
                 * if (min <= max) AND (min >= 0) AND (inv >= min) AND (inv <= max)
                 */
                if (Integer.parseInt(partMinField.getText()) <= Integer.parseInt(partMaxField.getText()) &&
                        Integer.parseInt(partMinField.getText()) >= 0 &&
                        Integer.parseInt(partInvField.getText()) >= Integer.parseInt(partMinField.getText()) &&
                        Integer.parseInt(partInvField.getText()) <= Integer.parseInt(partMaxField.getText()) &&
                        Integer.parseInt(partMaxField.getText()) >= 0) {

                    /**
                     * Checks what type of part is being created via a check of the radio button status, with this being an in-house part.
                     */
                    if (btnInHouse.isSelected() && !btnOutsourced.isSelected()) {
                        /**
                         * Sets the associated parameters via the text field inputs.
                         */
                        InHousePart newInHousePart = new InHousePart(
                                1/*placeholder #*/,
                                partNameField.getText(),
                                Double.parseDouble(partPriceField.getText()),
                                Integer.parseInt(partInvField.getText()),
                                Integer.parseInt(partMinField.getText()),
                                Integer.parseInt(partMaxField.getText()),
                                Integer.parseInt(partMachineIdField.getText())
                        );

                        /**
                         * Add 1 to the latest Id and set the new part Id to it for a contiguous unique ID.
                         */
                        Inventory.incrementPartLatestId();
                        newInHousePart.setId(Inventory.getPartLatestId());

                        /**
                         * Add to Parts ObservableList, shows up in Tableview.
                         */
                        Inventory.addPart(newInHousePart);
                    }
                    /**
                     * Checks what type of part is being created via a check of the radio button status,
                     * with this being an outsourced part.
                     */
                    else if (btnOutsourced.isSelected() && !btnInHouse.isSelected()) {
                        /**
                         * Sets the associated parameters via the text field inputs.
                         */
                        OutsourcedPart newOutsourcedPart = new OutsourcedPart(
                                1/*placeholder #*/,
                                partNameField.getText(),
                                Double.parseDouble(partPriceField.getText()),
                                Integer.parseInt(partInvField.getText()),
                                Integer.parseInt(partMinField.getText()),
                                Integer.parseInt(partMaxField.getText()),
                                partCompanyNameField.getText()
                        );

                        /**
                         * Add 1 to the latest Id and set the new part Id to it for a contiguous unique ID.
                         */
                        Inventory.incrementPartLatestId();
                        newOutsourcedPart.setId(Inventory.getPartLatestId());

                        /**
                         * Add to Parts ObservableList, shows up in Tableview.
                         */
                        Inventory.addPart(newOutsourcedPart);
                    }

                    /**
                     * Closes the window using the Save button's window/location.
                     */
                    Stage partAddStage = (Stage) btnSave.getScene().getWindow();
                    partAddStage.close();
                }
                /**
                 * Displays error messages for each logical error involving minimum, maximum, and inventory numbers.
                 */
                else {
                    if (Integer.parseInt(partMinField.getText()) >= Integer.parseInt(partMaxField.getText())) {
                        showMessageDialog(null, "Error: Minimum cannot be greater than Maximum.");
                    }
                    if (Integer.parseInt(partMinField.getText()) < 0) {
                        showMessageDialog(null, "Error: Minimum cannot be less than zero.");
                    }
                    if (Integer.parseInt(partInvField.getText()) < Integer.parseInt(partMinField.getText())) {
                        showMessageDialog(null, "Error: Inventory cannot be less than Minimum.");
                    }
                    if (Integer.parseInt(partInvField.getText()) > Integer.parseInt(partMaxField.getText())) {
                        showMessageDialog(null, "Error: Inventory cannot be greater than Maximum.");
                    }
                    if (Integer.parseInt(partMaxField.getText()) < 0) {
                        showMessageDialog(null, "Error: Maximum cannot be less than zero.");
                    }
                }
                }
            else {
                showMessageDialog(null,"Please choose either the In-House or Outsourced buttons.");
            }
        }
            /**
             * Catches the error when anything other than numerals are used in the text fields.
             */
        catch(NumberFormatException e){
                showMessageDialog(null, "Invalid input. Please provide only numbers for Price, Minimum, Maximum, and Inventory.");
            }
    }

    /**
     * Actions done when this part of the program is loaded.
     */
    @FXML
    void initialize() {
        /**
         * Sets in-house part as the default selected option when opening up the Add Part menu.
         */
        btnInHouse.setSelected(true);
        btnOutsourced.setSelected(false);
        partCompanyNameLabel.setVisible(false);
        partCompanyNameField.setVisible(false);
        partMachineIdLabel.setVisible(true);
        partMachineIdField.setVisible(true);
        partIDField.setDisable(true);
    }
}
