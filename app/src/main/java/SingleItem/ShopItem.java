package SingleItem;

import android.graphics.drawable.Drawable;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ShopItem {
    private Double dblDistance;
    String strShopName, strShortDescript, strFullDescript, strAveTime, strOperatingHRS, strLogoSmall, strLogoBig,
            strAddress;
    LatLng llLocation;
    int intID = -1, intRating, intStatus;
    Drawable draStatus;
    private int intLikes = 0, isLiked, intAveTimeColor;
    private ArrayList<IngredientItem> ingredientItems;
    private ArrayList<MenuItem> menuItems;

    ShopItem() {
    }

    public ShopItem(int intID, String strShopName, String intLogoSmall, String intLogoBig, String strShortDescript,
                    String strFullDescript, LatLng llLocation, String strAddress, Double dblDistance, String strAveTime,
                    int intRating, String strOperatingHRS, int intLikes, int isLiked, int intAveTimeColor, int intStatus,
                    Drawable draStatus) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.strLogoSmall = intLogoSmall;
        this.strLogoBig = intLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.llLocation = llLocation;
        this.strAddress = strAddress;
        this.dblDistance = dblDistance;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
        this.intStatus = intStatus;
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


    public String getStrAveTime() {
        return strAveTime;
    }

    public int getIntRating() {
        return intRating;
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

    public int getIntAveTimeColor() {
        return intAveTimeColor;
    }

    public int getIntStatus() {
        return intStatus;
    }

    public Drawable getDraStatus() {
        return draStatus;
    }

    public void setStrAveTime(String strAveTime) {
        this.strAveTime = strAveTime;
    }

    public String getStrLogoSmall() {
        return strLogoSmall;
    }

    public void setStrLogoSmall(String strLogoSmall) {
        this.strLogoSmall = strLogoSmall;
    }

    public String getStrLogoBig() {
        return strLogoBig;
    }

    public void setStrLogoBig(String strLogoBig) {
        this.strLogoBig = strLogoBig;
    }

    public LatLng getLlLocation() {
        return llLocation;
    }

    public void setLlLocation(LatLng llLocation) {
        this.llLocation = llLocation;
    }

    public void setIntRating(int intRating) {
        this.intRating = intRating;
    }

    public void setIntStatus(int intStatus) {
        this.intStatus = intStatus;
    }

    public void setDraStatus(Drawable draStatus) {
        this.draStatus = draStatus;
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

    public void setIntAveTimeColor(int intAveTimeColor) {
        this.intAveTimeColor = intAveTimeColor;
    }

    public String getStrAddress() {
        return strAddress;
    }

    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    public Double getDblDistance() {
        return dblDistance;
    }

    public void setDblDistance(Double dblDistance) {
        this.dblDistance = dblDistance;
    }
}

