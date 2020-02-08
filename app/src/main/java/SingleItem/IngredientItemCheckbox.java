package SingleItem;

public class IngredientItemCheckbox {
    private int strID;
    private String strIngredientName;
    private Boolean isChecked;

    public IngredientItemCheckbox(int strID, String strIngredientName, Boolean isChecked) {
        this.strID = strID;
        this.strIngredientName = strIngredientName;
        this.isChecked = isChecked;
    }

    public int getStrID() {
        return strID;
    }

    public String getStrIngredientName() {
        return strIngredientName;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

}
