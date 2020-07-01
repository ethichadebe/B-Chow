package SingleItem;

import android.graphics.drawable.Drawable;

import static util.HelperMethods.convertedDateTime;

public class AdminOrderItemPast extends PastOrderItem {
    private String strTime, strFeedback;

    public AdminOrderItemPast(int intID, int intOderNum, String strTime, String strMenu, String strExtras, int intRating, String strFeedback,
                              Double dblPrice, Drawable isSelected) {
        this.intID = intID;
        this.intOderNum = intOderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.intRating = intRating;
        this.strFeedback = strFeedback;
        this.dblPrice = dblPrice;
        this.isSelected = isSelected;
    }

    public String getStrTime() {
        return convertedDateTime(strTime);
    }

    public String getStrFeedback() {
        return strFeedback;
    }

    public void setStrFeedback(String strFeedback) {
        this.strFeedback = strFeedback;
    }
}
