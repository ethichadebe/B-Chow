package SingleItem;

public class ShopItem {
    private int intID;
    private String strShopName;
    private int intLogo;
    private String strShortDescript;
    private int int1Star;
    private int int2Star;
    private int int3Star;
    private int int4Star;
    private int int5Star;
    private String strDistance;
    private String strAveTime;

    public ShopItem(int intID, String strShopName, int intLogo, String strShortDescript, int int1Star, int int2Star, int int3Star, int int4Star, int int5Star, String strDistance, String strAveTime) {
        this.intID = intID;
        this.strShopName = strShopName;
        this.intLogo = intLogo;
        this.strShortDescript = strShortDescript;
        this.int1Star = int1Star;
        this.int2Star = int2Star;
        this.int3Star = int3Star;
        this.int4Star = int4Star;
        this.int5Star = int5Star;
        this.strDistance = strDistance;
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

    public int getInt1Star() {
        return int1Star;
    }

    public void setInt1Star(int int1Star) {
        this.int1Star = int1Star;
    }

    public int getInt2Star() {
        return int2Star;
    }

    public void setInt2Star(int int2Star) {
        this.int2Star = int2Star;
    }

    public int getInt3Star() {
        return int3Star;
    }

    public void setInt3Star(int int3Star) {
        this.int3Star = int3Star;
    }

    public int getInt4Star() {
        return int4Star;
    }

    public void setInt4Star(int int4Star) {
        this.int4Star = int4Star;
    }

    public int getInt5Star() {
        return int5Star;
    }

    public void setInt5Star(int int5Star) {
        this.int5Star = int5Star;
    }

    public String getStrDistance() {
        return strDistance;
    }

    public void setStrDistance(String strDistance) {
        this.strDistance = strDistance;
    }

    public String getStrAveTime() {
        return strAveTime;
    }

    public void setStrAveTime(String strAveTime) {
        this.strAveTime = strAveTime;
    }
}
