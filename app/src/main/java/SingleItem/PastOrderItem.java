package SingleItem;

import static util.HelperMethods.convertedDateTime;

public class PastOrderItem {
    private int intID, sID, intOrderNum, intRating;
    private String strShopName, strTime, strMenu, strExtras;
    private String strStatus = "";
    private Double dblPrice;

    public PastOrderItem(int intID, int sID, int intOrderNum, int intRating, String strShopName, String strTime,
                         String strMenu, String strExtras, Double dblPrice, String strStatus) {
        this.intID = intID;
        this.sID = sID;
        this.intOrderNum = intOrderNum;
        this.intRating = intRating;
        this.strShopName = strShopName;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.dblPrice = dblPrice;
        if(strStatus.equals("Cancelled")){
            this.strStatus = strStatus;
        }
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public String getStrShopName() {
        return strShopName;
    }

    public void setStrShopName(String strShopName) {
        this.strShopName = strShopName;
    }

    public int getIntOrderNum() {
        return intOrderNum;
    }

    public void setIntOrderNum(int intOrderNum) {
        this.intOrderNum = intOrderNum;
    }

    public String getStrTime() {
        return convertedDateTime(strTime);
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getStrMenu() {
        return strMenu;
    }

    public void setStrMenu(String strMenu) {
        this.strMenu = strMenu;
    }

    public Double getDblPrice() {
        return dblPrice;
    }

    public void setDblPrice(Double dblPrice) {
        this.dblPrice = dblPrice;
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

    public String getStrExtras() {
        return strExtras;
    }

    public void setStrExtras(String strExtras) {
        this.strExtras = strExtras;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }
}
