package Inventory;

public class InHousePart extends Part {
    /**
     * This is the child-class of Part where the difference is that it is an in-house part and requires a machine ID.
     */
    private int machineId;

    /**
     * @param id the part id.
     * @param name the part name.
     * @param price the part price.
     * @param stock the price stock/inventory level.
     * @param min the part minimum count.
     * @param max the part maximum count.
     * @param machineId the part's machine id
     */
    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        /**
         * Utilises all variables of the parent class, except for machine ID which is specific to the child-class.
         */

        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * Sets the machine ID parameter.
     * @param machineId the machine id to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * Give the machine ID of the specific part.
     * @return return part's the machine id
     */
    public int getMachineId(){
        return machineId;
    }

}