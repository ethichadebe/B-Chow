package SingleItem;

public class CancelledOrderItem extends MenuStatItem{
    protected String strReason;

    public String getStrReason() {
        return strReason;
    }

    public void setStrReason(String strReason) {
        this.strReason = strReason;
    }

    public CancelledOrderItem(double dblPrice, String strMenu, String strReason) {
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
        this.strReason = strReason;
    }
}

