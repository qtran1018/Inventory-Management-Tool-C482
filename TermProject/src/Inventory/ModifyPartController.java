package Inventory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.soap.Text;

import static javax.swing.JOptionPane.showMessageDialog;

public class ModifyPartController {

    /**
     * Declared part for use in the initPartData method below, between ModifyPartController and InventoryController.
     */
    public Part part;

    /**
     * Declares all applicable FXML variables used and displayed in the window.
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

    @FXML
    void inHouse(ActionEvent event) {
        /**
         * Sets in-house machine vars as selected, deselects company-name vars.
         * The fields overlap in the FXML file, so setting visibility
         * is needed to ensure the proper field is typed into
         */
        btnOutsourced.setSelected(false);
        partCompanyNameLabel.setVisible(false);
        partCompanyNameField.setVisible(false);
        partMachineIdLabel.setVisible(true);
        partMachineIdField.setVisible(true);
    }

    @FXML
    void outSourced(ActionEvent event) {
        /**
         * Sets in-house company vars as selected, deselects machine vars.
         */

        btnInHouse.setSelected(false);
        partMachineIdLabel.setVisible(false);
        partMachineIdField.setVisible(false);
        partCompanyNameLabel.setVisible(true);
        partCompanyNameField.setVisible(true);
    }

    @FXML
    void partCancel(ActionEvent event) {
        /**
         * Uses the location of the cancel button to find the window it is in and closes it.
         */
        Stage partModifyStage = (Stage) btnCancel.getScene().getWindow();
        //Closes current window
        partModifyStage.close();
    }

    public void initPartData(Part part)
    /**
     * This is a method which is used to take a selected part from Inventory when modifying so its contents can be accessed in ModifyPartController.
     * Uses the declared Part variable at the top as a clone of the selected part, and pre-sets applicable fields when the window is opened.
     */
    {
        this.part = part;
        partIDField.setText(Integer.toString(part.getId()));
        partNameField.setText(part.getName());
        partPriceField.setText(Double.toString(part.getPrice()));
        partInvField.setText(Integer.toString(part.getStock()));
        partMinField.setText(Integer.toString(part.getMin()));
        partMaxField.setText(Integer.toString(part.getMax()));
        partIDField.setDisable(true);

        /**
         * Gets specific info, whether or not it is an in-house or outsourced part, by checking if machine ID is a valid number.
         */
        if (part instanceof InHousePart) {
            btnInHouse.setSelected(true);
            btnOutsourced.setSelected(false);
            /**
             * Access the selected part's child-class method by casting the entire part to the child-class, determined by the if-statement check.
             */
            partMachineIdField.setText(Integer.toString(((InHousePart)part).getMachineId()));
            partCompanyNameLabel.setVisible(false);
            partCompanyNameField.setVisible(false);
            partMachineIdLabel.setVisible(true);
            partMachineIdField.setVisible(true);
        }
        else if (part instanceof OutsourcedPart) {
            btnInHouse.setSelected(false);
            btnOutsourced.setSelected(true);
            partCompanyNameField.setText(((OutsourcedPart)part).getCompanyName());
            partCompanyNameLabel.setVisible(true);
            partCompanyNameField.setVisible(true);
            partMachineIdLabel.setVisible(false);
            partMachineIdField.setVisible(false);
        }

        else {
            showMessageDialog(null, "Somehow, the part is neither in-house or outsourced. Please contact the developer.");
        }

    }

    @FXML
    void partSave(ActionEvent event) {

        int indexPart = Inventory.getAllParts().indexOf(part);

        try {
            /**
             * Checks the logic between the minimum, maximum, and inventory fields before setting those values to the part, using the logic --
             * if (min <= max) AND (min >= 0) AND (inv >= min) AND (inv <= max)
             */
            if (btnInHouse.isSelected() || btnOutsourced.isSelected()) {
                if (Integer.parseInt(partMinField.getText()) <= Integer.parseInt(partMaxField.getText()) &&
                        Integer.parseInt(partMinField.getText()) >= 0 &&
                        Integer.parseInt(partInvField.getText()) >= Integer.parseInt(partMinField.getText()) &&
                        Integer.parseInt(partInvField.getText()) <= Integer.parseInt(partMaxField.getText()) &&
                        Integer.parseInt(partMaxField.getText()) >= 0) {

                    /**
                     * Determines via the radio buttons if the part should be a in-house or outsourced part.
                     */
                    if (btnInHouse.isSelected() && !btnOutsourced.isSelected()) {
                        /**
                         * Allow switching of part-type by remaking a part using the modified part's ID and overwriting the old part with the new.
                         */
                        InHousePart newInHousePart = new InHousePart(
                                /**
                                 * Use old part's ID, which is disabled for editing, and update the rest.
                                 */
                                part.getId(),
                                partNameField.getText(),
                                Double.parseDouble(partPriceField.getText()),
                                Integer.parseInt(partInvField.getText()),
                                Integer.parseInt(partMinField.getText()),
                                Integer.parseInt(partMaxField.getText()),
                                Integer.parseInt(partMachineIdField.getText())
                        );
                        /**
                         * Uses the updatePart method in InventoryController to update the values of the selected part with those above.
                         */
                        Inventory.updatePart(indexPart, newInHousePart);

                        /**
                         * Closes the window.
                         */
                        Stage partModifyStage = (Stage) btnSave.getScene().getWindow();
                        partModifyStage.close();
                    }
                    /**
                     * Determines via the radio buttons if the part should be a in-house or outsourced part.
                     */
                    else if (btnOutsourced.isSelected() && !btnInHouse.isSelected()) {
                        /**
                         * Allow switching of part-type by remaking a part using the modified part's ID and overwriting the old part with the new.
                         */
                        OutsourcedPart newOutsourcedPart = new OutsourcedPart(
                                /**
                                 * Use old part's ID, which is disabled for editing, and update the rest.
                                 */
                                part.getId(),
                                partNameField.getText(),
                                Double.parseDouble(partPriceField.getText()),
                                Integer.parseInt(partInvField.getText()),
                                Integer.parseInt(partMinField.getText()),
                                Integer.parseInt(partMaxField.getText()),
                                partCompanyNameField.getText()
                        );
                        Inventory.updatePart(indexPart, newOutsourcedPart);

                        //Close the window/stage
                        Stage partModifyStage = (Stage) btnSave.getScene().getWindow();
                        partModifyStage.close();
                    }
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
                showMessageDialog(null, "Please choose either the In-House or Outsourced buttons.");
            }
        }
        /**
         * Catches the error when anything other than numerals are used in the text fields.
         */
        catch (NumberFormatException e) {
            showMessageDialog(null, "Invalid input. Please provide only numbers for Price, Minimum, Maximum, and Inventory.");
        }
    }

    @FXML
    void initialize() {
        /*
        btnInHouse.setSelected(true);
        btnOutsourced.setSelected(false);
        partCompanyNameLabel.setVisible(false);
        partCompanyNameField.setVisible(false);
        partMachineIdLabel.setVisible(true);
        partMachineIdField.setVisible(true);
        partIDField.setDisable(true);
        */
    }

}


