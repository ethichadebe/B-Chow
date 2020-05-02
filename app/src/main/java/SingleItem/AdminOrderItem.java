package SingleItem;

import static util.HelperMethods.convertedDateTime;
import static util.HelperMethods.convertedTime;

public class AdminOrderItem {
    private int intID;
    private int intOderNum;
    private String strTime,strMenu,strExtras,strStatus;
    private Double dblPrice;

    public AdminOrderItem(int intID, int intOderNum, String strTime, String strMenu, String strExtras, String strStatus,
                          Double dblPrice) {
        this.intID = intID;
        this.intOderNum = intOderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.strStatus = strStatus;
        this.dblPrice = dblPrice;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public int getIntOderNum() {
        return intOderNum;
    }

    public void setIntOderNum(int intOderNum) {
        this.intOderNum = intOderNum;
    }

    public String getStrTrime() {
        return convertedTime(strTime);
    }

    public void setStrTime(String stTrime) {
        this.strTime = strTime;
    }

    public Double getDblPrice() {
        return dblPrice;
    }

    public void setDblPrice(Double dblPrice) {
        this.dblPrice = dblPrice;
    }

    public String getStrMenu() {
        return strMenu;
    }

    public void setStrMenu(String strMenu) {
        this.strMenu = strMenu;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrExtras() {
        return strExtras;
    }

    public void setStrExtras(String strExtras) {
        this.strExtras = strExtras;
    }
}
