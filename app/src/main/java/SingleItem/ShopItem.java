package SingleItem;

import android.graphics.drawable.Drawable;
import android.location.Location;

import java.util.ArrayList;

public class ShopItem {
    private String strShopName,strShortDescript,strFullDescript,strAveTime,strOperatingHRS,locLocation;
    private int intID,intLogoSmall,intLogoBig,intRating,isLiked, intAveTimeColor;
    private int intLikes = 0;
    private String strStatus = "";
    private Drawable draStatus;
    private ArrayList<IngredientItem> ingredientItems;
    private ArrayList<MenuItem> menuItems;

    public ShopItem(int intID, String strShopName, int intLogoSmall, int intLogoBig, String strShortDescript,
                    String strFullDescript, String locLocation, String strAveTime, int intRating, String strOperatingHRS,
                    int intLikes, int isLiked, int intAveTimeColor, String strStatus, Drawable draStatus) {
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
        this.strStatus = strStatus;
        this.draStatus = draStatus;
        this.intLikes = intLikes;
        this.isLiked = isLiked;
        this.intAveTimeColor = intAveTimeColor;
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

    public String getLocLocation() {
        return locLocation;
    }

    public void setLocLocation(String locLocation) {
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

    public int isLiked() {
        return isLiked;
    }

    public void setLiked(int liked) {
        isLiked = liked;
    }

    public int getIntLikes() {
        return intLikes;
    }

    public void setIntLikes(int intLikes) {
        this.intLikes = intLikes;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getIntAveTimeColor() {
        return intAveTimeColor;
    }

    public void setIntAveTimeColor(int intAveTimeColor) {
        this.intAveTimeColor = intAveTimeColor;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public Drawable getDraStatus() {
        return draStatus;
    }

    public void setDraStatus(Drawable draStatus) {
        this.draStatus = draStatus;
    }
}

