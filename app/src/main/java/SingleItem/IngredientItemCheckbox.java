package SingleItem;

public class IngredientItemCheckbox {
    private int strID;
    private String strIngredientName;
    private Double dblPrice;
    private Boolean isChecked;

    public IngredientItemCheckbox(int strID, String strIngredientName, Double dblPrice, Boolean isChecked) {
        this.strID = strID;
        this.strIngredientName = strIngredientName;
        this.dblPrice = dblPrice;
        this.isChecked = isChecked;
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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
