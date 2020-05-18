package SingleItem;

public class AdminOrderItemPast extends PastOrderItem {
    private String strTime, strFeedback;

    public AdminOrderItemPast(int intID, int intOderNum, String strTime, String strMenu, String strExtras, String strFeedback,
                              Double dblPrice) {
        this.intID = intID;
        this.intOderNum = intOderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.strExtras = strExtras;
        this.strFeedback = strFeedback;
        this.dblPrice = dblPrice;
    }

    public String getStrTime() {
        return strTime;
    }

    public String getStrFeedback() {
        return strFeedback;
    }

    public void setStrFeedback(String strFeedback) {
        this.strFeedback = strFeedback;
    }
}
