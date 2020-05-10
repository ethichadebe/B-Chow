package SingleItem;

import static util.HelperMethods.convertedTime;

public class UpcomingOrderItem extends OrderItem{
    private String strShopName;
    private String strTime;

    public UpcomingOrderItem(int intID, String strShopName, int intOrderNum, String strTime, String strMenu,String strExtras, Double dblPrice,
                             String strStatus) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intOderNum = intOrderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.dblPrice = dblPrice;
        this.strExtras = strExtras;
        this.strStatus = strStatus;
    }

    public String getStrShopName() {
        return strShopName;
    }

    public void setStrShopName(String strShopName) {
        this.strShopName = strShopName;
    }

    public String getStrTime() {
        return convertedTime(strTime);
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

}
