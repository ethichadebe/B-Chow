package SingleItem;

public class IngredientItem {
    private int intID;
    private String strIngredientName;
    private  Double dblPrice;

    public IngredientItem(int intID, String strIngredientName, Double dblPrice) {
        this.intID = intID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
    }

    public IngredientItem(String strIngredientName) {
        this.strIngredientName = strIngredientName;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public String getStrIngredientName() {
        return strIngredientName;
    }

    public void setStrIngredientName(String strIngredientName) {
        this.strIngredientName = strIngredientName;
    }

    public Double getDblPrice() {
        return dblPrice;
    }

    public void setDblPrice(Double dblPrice) {
        this.dblPrice = dblPrice;
    }
}
