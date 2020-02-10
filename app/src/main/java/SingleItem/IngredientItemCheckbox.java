package SingleItem;

public class IngredientItemCheckbox {
    private int intID;
    private String strIngredientName;
    private Double dblPrice;
    private Boolean isChecked, Clickability;

    public IngredientItemCheckbox(int intID, String strIngredientName, Double dblPrice, Boolean isChecked, Boolean clickability) {
        this.intID = intID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
        this.isChecked = isChecked;
        Clickability = clickability;
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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getClickability() {
        return Clickability;
    }

    public void setClickability(Boolean clickability) {
        Clickability = clickability;
    }
}
