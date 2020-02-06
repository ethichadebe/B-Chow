package SingleItem;

public class IngredientItemCheckbox {
    private int strID;
    private String strIngredientName;
    private Boolean isChecked = true;

    public IngredientItemCheckbox(int strID, String strIngredientName) {
        this.strID = strID;
        this.strIngredientName = strIngredientName;
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
