package SingleItem;

public class ShopItem {
    private int intID;
    private String strShopName;
    private int intLogo;
    private String strShortDescript;
    private int intRating;
    private String strLocation;
    private String strAveTime;

    public ShopItem(int intID, String strShopName, int intLogo, String strShortDescript, int intRating, String strLocation,
                    String strAveTime) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intLogo = intLogo;
        this.strShortDescript = strShortDescript;
        this.intRating = intRating;
        this.strLocation = strLocation;
        this.strAveTime = strAveTime;
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

    public int getIntLogo() {
        return intLogo;
    }

    public void setIntLogo(int intLogo) {
        this.intLogo = intLogo;
    }

    public String getStrShortDescript() {
        return strShortDescript;
    }

    public void setStrShortDescript(String strShortDescript) {
        this.strShortDescript = strShortDescript;
    }

    public int getIntRating() {
        return intRating;
    }

    public void setIntRating(int intRating) {
        this.intRating = intRating;
    }

    public String getStrLocation() {
        return strLocation;
    }

    public void setStrLocation(String strLocation) {
        this.strLocation = strLocation;
    }

    public String getStrAveTime() {
        return strAveTime;
    }

    public void setStrAveTime(String strAveTime) {
        this.strAveTime = strAveTime;
    }
}
