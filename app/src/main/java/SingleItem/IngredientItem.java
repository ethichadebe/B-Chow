package SingleItem;

public class IngredientItem {
    private int strID;
    private String strIngredientName;
    private  Double dblPrice;

    public IngredientItem(int strID, String strIngredientName, Double dblPrice) {
        this.strID = strID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
    }

    public int getStrID() {
        return strID;
    }

    public String getStrIngredientName() {
        return strIngredientName;
    }

    public Double getDblPrice() {
        return dblPrice;
    }
}
