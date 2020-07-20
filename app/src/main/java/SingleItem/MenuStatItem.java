package SingleItem;

public class MenuStatItem {
    protected double dblPrice;
    protected String strMenu;
    private int intnItems = 0;

    public Double getDblPrice() {
        return dblPrice;
    }

    public void setDblPrice(Double dblPrice) {
        this.dblPrice = dblPrice;
    }

    public String getStrMenu() {
        return strMenu;
    }

    public void setStrMenu(String strMenu) {
        this.strMenu = strMenu;
    }

    public int getIntnItems() {
        return intnItems;
    }

    public void setIntnItems(int intnItems) {
        this.intnItems = intnItems;
    }

    public MenuStatItem() {
    }

    public MenuStatItem(Double dblPrice, String strMenu, int intnItems) {
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
        this.intnItems = intnItems;
    }
}

