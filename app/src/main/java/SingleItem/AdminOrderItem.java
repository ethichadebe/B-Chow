package SingleItem;

import android.graphics.drawable.Drawable;

import static util.HelperMethods.convertedTime;

public class AdminOrderItem extends OrderItem {
    private String strTime;

    public AdminOrderItem(int intID, int intOderNum, String strTime, String strMenu, String strExtras, String strStatus,
                          Double dblPrice, Drawable isSelected) {
        this.intID = intID;
        this.intOderNum = intOderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.strStatus = strStatus;
        this.dblPrice = dblPrice;
        this.isSelected = isSelected;
    }

    public String getStrTrime() {
        return convertedTime(strTime);
    }

    public void setStrTime(String stTrime) {
        this.strTime = strTime;
    }
}
