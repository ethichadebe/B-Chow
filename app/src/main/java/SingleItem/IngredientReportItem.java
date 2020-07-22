package SingleItem;

public class IngredientReportItem implements Comparable<IngredientReportItem>{
    private String strName;
    private int intCount;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getIntCount() {
        return intCount;
    }

    public void setIntCount(int intCount) {
        this.intCount = intCount;
    }

    public IngredientReportItem(String strName, int intCount) {
        this.strName = strName;
        this.intCount = intCount;
    }

    @Override
    public int compareTo(IngredientReportItem o) {
        return o.intCount-this.intCount;
    }
}

