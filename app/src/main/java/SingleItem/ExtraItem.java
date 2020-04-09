package SingleItem;

public class ExtraItem {
    private int intID;
    private String strExtraName;

    public ExtraItem(int intID, String strExtraName) {
        this.intID = intID;
        this.strExtraName = strExtraName;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public String getStrExtraName() {
        return strExtraName;
    }

    public void setStrExtraName(String strExtraName) {
        this.strExtraName = strExtraName;
    }
}
