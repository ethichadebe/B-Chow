package SingleItem;

import android.location.Location;

import java.util.ArrayList;

public class ShopItem {
    private int intID;
    private String strShopName;
    private int intLogoSmall;
    private int intLogoBig;
    private String strShortDescript;
    private String strFullDescript;
    private Location locLocation;
    private String strAveTime;
    private int intRating;
    private String strOperatingHRS;
    private ArrayList<IngredientItem> ingredientItems;
    private ArrayList<MenuItem> menuItems;

    public ShopItem(int intID, String strShopName, int intLogoSmall, int intLogoBig, String strShortDescript,
                    String strFullDescript, Location locLocation, String strAveTime, int intRating, String strOperatingHRS) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.locLocation = locLocation;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
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

    public int getIntLogoSmall() {
        return intLogoSmall;
    }

    public void setIntLogoSmall(int intLogoSmall) {
        this.intLogoSmall = intLogoSmall;
    }

    public int getIntLogoBig() {
        return intLogoBig;
    }

    public void setIntLogoBig(int intLogoBig) {
        this.intLogoBig = intLogoBig;
    }

    public String getStrShortDescript() {
        return strShortDescript;
    }

    public void setStrShortDescript(String strShortDescript) {
        this.strShortDescript = strShortDescript;
    }

    public String getStrFullDescript() {
        return strFullDescript;
    }

    public void setStrFullDescript(String strFullDescript) {
        this.strFullDescript = strFullDescript;
    }

    public Location getLocLocation() {
        return locLocation;
    }

    public void setLocLocation(Location locLocation) {
        this.locLocation = locLocation;
    }

    public String getStrAveTime() {
        return strAveTime;
    }

    public void setStrAveTime(String strAveTime) {
        this.strAveTime = strAveTime;
    }

    public int getIntRating() {
        return intRating;
    }

    public void setIntRating(int intRating) {
        this.intRating = intRating;
    }

    public String getStrOperatingHRS() {
        return strOperatingHRS;
    }

    public void setStrOperatingHRS(String strOperatingHRS) {
        this.strOperatingHRS = strOperatingHRS;
    }

    public ArrayList<IngredientItem> getIngredientItems() {
        return ingredientItems;
    }

    public void setIngredientItems(ArrayList<IngredientItem> ingredientItems) {
        this.ingredientItems = ingredientItems;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

