package SingleItem;

public class MyShopItem {
    private String strShopName;
    private String strPosition;
    private int intLogo;
    private String strShortDescript;
    private int int1Star;
    private int int2Star;
    private int int3Star;
    private int int4Star;
    private int int5Star;
    private String strDistance;
    private String strAveTime;

    public MyShopItem(String strShopName, String strPosition, int intLogo, String strShortDescript, int int1Star, int int2Star,
                      int int3Star, int int4Star, int int5Star, String strDistance, String strAveTime) {
        this.strShopName = strShopName;
        this.strPosition = strPosition;
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

    public String getStrShopName() {
        return strShopName;
    }

    public String getStrPosition() {
        return strPosition;
    }

    public int getIntLogo() {
        return intLogo;
    }

    public String getStrShortDescript() {
        return strShortDescript;
    }

    public int getInt1Star() {
        return int1Star;
    }

    public int getInt2Star() {
        return int2Star;
    }

    public int getInt3Star() {
        return int3Star;
    }

    public int getInt4Star() {
        return int4Star;
    }

    public int getInt5Star() {
        return int5Star;
    }

    public String getStrDistance() {
        return strDistance;
    }

    public String getStrAveTime() {
        return strAveTime;
    }


}