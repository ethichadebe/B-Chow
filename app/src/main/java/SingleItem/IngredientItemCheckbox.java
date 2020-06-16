package SingleItem;

public class IngredientItemCheckbox {
    private int intID;
    private String strIngredientName;
    private Double dblPrice;
    private Boolean isChecked, isVisible, isClickable;

    public IngredientItemCheckbox(IngredientItem ingredientItem, boolean isChecked, boolean isVisible, boolean isClickable){
        this.intID = ingredientItem.getIntID();
        this.strIngredientName = ingredientItem.getStrIngredientName();
        this.dblPrice = ingredientItem.getDblPrice();
        this.isChecked = isChecked;
        this.isVisible = isVisible;
        this.isClickable = isClickable;
    }

    public IngredientItemCheckbox(int intID, String strIngredientName, Double dblPrice, boolean isChecked, boolean isVisible,
                                  boolean isClickable) {
        this.intID = intID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
        this.isChecked = isChecked;
        this.isVisible = isVisible;
        this.isClickable = isClickable;
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

    public void setChecked() {
        isChecked = !isChecked;
    }

    public Boolean getClickable() {
        return isClickable;
    }

    public void setClickable(Boolean clickable) {
        isClickable = clickable;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }
}
