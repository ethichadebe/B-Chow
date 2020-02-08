package SingleItem;

import android.graphics.drawable.Drawable;

public class IngredientItem {
    private int strID;
    private String strIngredientName;

    public IngredientItem(int strID, String strIngredientName) {
        this.strID = strID;
        this.strIngredientName = strIngredientName;
    }

    public int getStrID() {
        return strID;
    }

    public String getStrIngredientName() {
        return strIngredientName;
    }
}
