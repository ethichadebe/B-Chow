package SingleItem;

public class ExtraItem {
    private int intID;
    private String strIngredientName;

    public ExtraItem(int intID, String strIngredientName) {
        this.intID = intID;
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
}
