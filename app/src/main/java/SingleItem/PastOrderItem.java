package SingleItem;

import static util.HelperMethods.convertedDateTime;

public class PastOrderItem extends OrderItem {
    private int sID;
    int intRating;
    private String strShopName, strTime;

    PastOrderItem() {
    }

    public PastOrderItem(int intID, int sID, int intOrderNum, int intRating, String strShopName, String strTime,
                         String strMenu, String strExtras, Double dblPrice, String strStatus) {
        this.intID = intID;
        this.sID = sID;
        this.intOderNum = intOrderNum;
        this.intRating = intRating;
        this.strShopName = strShopName;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.dblPrice = dblPrice;
        this.strStatus = strStatus;
    }

    public String getStrShopName() {
        return strShopName;
    }

    public void setStrShopName(String strShopName) {
        this.strShopName = strShopName;
    }

    public String getStrTime() {
        return convertedDateTime(strTime);
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public int getIntRating() {
        return intRating;
    }

    public void setIntRating(int intRating) {
        this.intRating = intRating;
    }

    public int getsID() {
        return sID;
    }

    public void setsID(int sID) {
        this.sID = sID;
    }
}
