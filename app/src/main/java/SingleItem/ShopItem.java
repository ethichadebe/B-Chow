package SingleItem;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import static util.Constants.getIpAddress;

public class ShopItem {
    private Double dblDistance;
    protected String strShopName, strShortDescript, strFullDescript, strAveTime, strOperatingHRS, strLogoSmall,
            strLogoBig, strAddress;
    protected LatLng llLocation;
    protected int intID = -1, intStatus;
    protected Drawable draLogoSmall, draLogoBig;
    protected double intRating;
    private int intLikes = 0, isLiked, intAveTimeColor;
    private ArrayList<IngredientItem> ingredientItems;
    private ArrayList<MenuItem> menuItems;
    private boolean showAd;

    ShopItem() {
    }

    public ShopItem(int intID, String strShopName, String strLogoSmall, String strLogoBig,
                    String strShortDescript, String strFullDescript, LatLng llLocation, String strAddress,
                    Double dblDistance, String strAveTime, double intRating, String strOperatingHRS, int intLikes,
                    int isLiked, int intAveTimeColor, int intStatus,boolean showAd) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.strLogoSmall = strLogoSmall;
        this.strLogoBig = strLogoBig;
        this.strShortDescript = strShortDescript;
        this.strFullDescript = strFullDescript;
        this.llLocation = llLocation;
        this.strAddress = strAddress;
        this.dblDistance = dblDistance;
        this.strAveTime = strAveTime;
        this.intRating = intRating;
        this.strOperatingHRS = strOperatingHRS;
        this.intStatus = intStatus;
        this.intLikes = intLikes;
        this.isLiked = isLiked;
        this.intAveTimeColor = intAveTimeColor;
        this.showAd = showAd;
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

    public double getIntRating() {
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

    public void setIntRating(double intRating) {
        this.intRating = intRating;
    }

    public void setIntStatus(int intStatus) {
        this.intStatus = intStatus;
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

    public boolean isShowAd() {
        return showAd;
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    public Drawable getDraLogoSmall() {
        return draLogoSmall;
    }

    public void setDraLogoSmall(Drawable draLogoSmall) {
        this.draLogoSmall = draLogoSmall;
    }

    public Drawable getDraLogoBig() {
        return draLogoBig;
    }

    public void setDraLogoBig(Drawable draLogoBig) {
        this.draLogoBig = draLogoBig;
    }
}

