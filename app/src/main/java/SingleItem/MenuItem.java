package SingleItem;

public class MenuItem {
    private int strID;
    private Double dblPrice;
    private String strMenu;
    private int intEdit;
    private int intDelete;
    private int intVisibility;

    public MenuItem(int strID, Double strPrice, String strMenu, int intEdit, int intDelete, int intVisibility) {
        this.strID = strID;
        this.dblPrice = strPrice;
        this.strMenu = strMenu;
        this.intEdit = intEdit;
        this.intDelete = intDelete;
        this.intVisibility = intVisibility;
    }

    public int getStrID() {
        return strID;
    }

    public Double getDblPrice() {
        return dblPrice;
    }

    public String getStrMenu() {
        return strMenu;
    }

    public int getIntEdit() {
        return intEdit;
    }

    public int getIntDelete() {
        return intDelete;
    }

    public int getIntVisibility() {
        return intVisibility;
    }

    public void EditPriceNMenu(Double price, String Menu){
        strMenu = Menu;
        dblPrice = price;
    }
}