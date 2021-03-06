package SingleItem;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import static util.HelperMethods.convertedTime;

public class UpcomingOrderItem extends OrderItem{
    private String strShopName;
    private String strTime;
    private LatLng llShop;

    public UpcomingOrderItem(int intID, String strShopName, int intOrderNum, String strTime, String strMenu, String strExtras, Double dblPrice,
                             String strStatus, LatLng llShop, Drawable isSelected, int statusColor) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intOderNum = intOrderNum;
        this.strTime = strTime;
        this.strMenu = strMenu;
        this.dblPrice = dblPrice;
        this.strExtras = strExtras;
        this.strStatus = strStatus;
        this.llShop = llShop;
        this.isSelected = isSelected;
        this.statusColor = statusColor;
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

    public LatLng getLlShop() {
        return llShop;
    }

    public void setLlShop(LatLng llShop) {
        this.llShop = llShop;
    }
}
