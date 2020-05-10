package SingleItem;

import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import www.ethichadebe.com.loxion_beanery.R;

public class MyShopItem extends ShopItem{
    private String strPosition = "";
    private int intnOrders;
    private boolean isActive = false;

    /**
     * Standard constructor + operating Hours
     *
     * @param intID
     * @param strShopName
     * @param strPosition
     * @param intLogoSmall
     * @param intLogoBig
     * @param strShortDescript
     * @param strFullDescript
     * @param locLocation
     * @param strAveTime
     * @param intRating
     * @param strOperatingHRS
     */
    public MyShopItem(int intID, String strShopName, String strPosition, String intLogoSmall, String intLogoBig,
                      String strShortDescript, String strFullDescript, String locLocation, String strAveTime,
                      int intRating, String strOperatingHRS, boolean isActive, int intStatus, Drawable draStatus,
                      int intnOrders) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.strPosition = strPosition;
        this.strLogoSmall = intLogoSmall;
        this.strLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.strLocation = locLocation;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
        this.isActive = isActive;
        this.intStatus = intStatus;
        this.draStatus = draStatus;
        this.intnOrders = intnOrders;
    }

    /**
     * Shop Registration constructor
     *
     * @param strShopName
     * @param intLogoSmall
     * @param intLogoBig
     * @param strShortDescript
     * @param strFullDescript
     * @param locLocation
     */
    public MyShopItem(String strShopName, String strShortDescript, String strFullDescript, String intLogoSmall,
                      String  intLogoBig, String locLocation) {
        this.strShopName = strShopName;
        this.strLogoSmall = intLogoSmall;
        this.strLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.strLocation = locLocation;
    }

    public String getStrPosition() {
        return strPosition;
    }

    public void setStrPosition(String strPosition) {
        this.strPosition = strPosition;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getIntnOrders() {
        return intnOrders;
    }

    public void setIntnOrders(int intnOrders) {
        this.intnOrders = intnOrders;
    }
}