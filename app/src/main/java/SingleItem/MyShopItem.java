package SingleItem;

import android.location.Location;

import java.util.ArrayList;

public class MyShopItem {
    private int intID = -1;
    private String strShopName, strOperatingHRS,strPosition,strShortDescript,strFullDescript,strAveTime;
    private int intLogoSmall,intLogoBig,intRating;
    private Location locLocation;
    private boolean isOpen = false;
    private boolean isActive = false;
    private ArrayList<IngredientItem> ingredientItems;
    private ArrayList<MenuItem> menuItems;

    /**
     * Standard constructor
     *
     * @param intID
     * @param strShopName
     * @param intLogoSmall
     * @param intLogoBig
     * @param strShortDescript
     * @param strFullDescript
     * @param locLocation
     * @param strAveTime
     * @param intRating
     */
    public MyShopItem(int intID, String strShopName, int intLogoSmall, int intLogoBig, String strShortDescript,
                      String strFullDescript, Location locLocation, String strAveTime, int intRating) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.locLocation = locLocation;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
    }

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
    public MyShopItem(int intID, String strShopName, String strPosition, int intLogoSmall, int intLogoBig,
                      String strShortDescript, String strFullDescript, Location locLocation, String strAveTime, int intRating,
                      String strOperatingHRS, boolean isActive) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.strPosition = strPosition;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.locLocation = locLocation;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
        this.isActive = isActive;
    }

    /**
     * default constructor
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
     * @param ingredientItems
     * @param menuItems
     */
    public MyShopItem(int intID, String strShopName, String strPosition, int intLogoSmall, int intLogoBig,
                      String strShortDescript, String strFullDescript, Location locLocation, String strAveTime, int intRating,
                      String strOperatingHRS, ArrayList<IngredientItem> ingredientItems, ArrayList<MenuItem> menuItems) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.strPosition = strPosition;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
        this.ingredientItems = ingredientItems;
        this.menuItems = menuItems;
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
    public MyShopItem(String strShopName, String strShortDescript, String strFullDescript, int intLogoSmall, int intLogoBig,
                      Location locLocation) {
        this.strShopName = strShopName;
        this.intLogoSmall = intLogoSmall;
        this.intLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.locLocation = locLocation;
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

    public String getStrPosition() {
        return strPosition;
    }

    public void setStrPosition(String strPosition) {
        this.strPosition = strPosition;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}