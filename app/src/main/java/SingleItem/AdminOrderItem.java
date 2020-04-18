package SingleItem;

public class AdminOrderItem {
    private int intID;
    private int intOderNum;
    private String strTrime,strMenu,strStatus;
    private Double dblPrice;

    public AdminOrderItem(int intID, int intOderNum, String strTrime, Double dblPrice, String strMenu, String strStatus) {
        this.intID = intID;
        this.intOderNum = intOderNum;
        this.strTrime = strTrime;
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
        this.strStatus = strStatus;
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
        return strTrime;
    }

    public void setStrTrime(String strTrime) {
        this.strTrime = strTrime;
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
}
