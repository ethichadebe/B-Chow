package SingleItem;

import com.google.android.gms.maps.model.LatLng;

public class OrderItem {
    protected int intID,intOderNum;
    protected Double dblPrice;
    protected String strMenu,strExtras = "",strStatus;
    protected LatLng llShopLocation;

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

    public LatLng getLlShopLocation() {
        return llShopLocation;
    }

    public void setLlShopLocation(LatLng llShopLocation) {
        this.llShopLocation = llShopLocation;
    }
}
