package SingleItem;

public class UpcomingOrderItem {
    private int intID,intOrderNum;
    private String strShopName;
    private String strTime;
    private String strMenu;
    private String strExtras;
    private String  strStatus;
    private Double dblPrice;

    public UpcomingOrderItem(int intID, String strShopName, int intOrderNum, String strTime, String strMenu,String strExtras, Double dblPrice,
                             String strStatus) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intOrderNum = intOrderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.dblPrice = dblPrice;
        this.strExtras = strExtras;
        this.strStatus = strStatus;
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
        return strTime;
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
