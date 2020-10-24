package Inventory;

public class OutsourcedPart extends Part{
    /**
     * This is the child-class of Part where the difference is that it is an outsourced part and requires a comapny name.
     */

    private String companyName;

    /**
     * Utilises all variables of the parent class, except for company name, which is specific to the child-class.
     */
    /**
     *
     * @param id the part id.
     * @param name the part name.
     * @param price the part price.
     * @param stock the price stock/inventory level.
     * @param min the part minimum count.
     * @param max the part maximum count.
     * @param companyName the outsourced part's company of origin.
     */
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        setCompanyName(companyName);
    }

    /**
     * Sets the company name parameter to the part.
     * @param companyName the company name to set.
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * Gives the company name of the specific part.
     * @return return the company name
     */
    public String getCompanyName(){
        return companyName;
    }

}