package SingleItem;

public class IngredientItemCheckbox {
    private int intID;
    private String strIngredientName;
    private Double dblPrice;
    private Boolean isChecked;

    public IngredientItemCheckbox(IngredientItem ingredientItem, boolean isChecked){
        this.intID = ingredientItem.getIntID();
        this.strIngredientName = ingredientItem.getStrIngredientName();
        this.dblPrice = ingredientItem.getDblPrice();
        this.isChecked = isChecked;
    }

    public IngredientItemCheckbox(int intID, String strIngredientName, Double dblPrice, Boolean isChecked) {
        this.intID = intID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
        this.isChecked = isChecked;
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
}
