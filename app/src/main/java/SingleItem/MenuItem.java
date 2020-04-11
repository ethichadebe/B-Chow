package SingleItem;

public class MenuItem {
    private int intID;
    private Double dblPrice;
    private String strMenu;
    private boolean isVisible = false;

    public MenuItem(int intID, Double dblPrice, String strMenu) {
        this.intID = intID;
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
    }

    public MenuItem(int intID, Double dblPrice, String strMenu, boolean isVisible) {
        this.intID = intID;
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
        this.isVisible = isVisible;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

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

    public void EditPriceNMenu(Double price, String Menu){
        strMenu = Menu;
        dblPrice = price;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}