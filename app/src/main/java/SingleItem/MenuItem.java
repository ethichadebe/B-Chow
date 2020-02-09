package SingleItem;

public class MenuItem {
    private int intID;
    private Double dblPrice;
    private String strMenu;
    private int intEdit;
    private int intDelete;
    private int intVisibility;

    public MenuItem(int intID, Double dblPrice, String strMenu, int intEdit, int intDelete, int intVisibility) {
        this.intID = intID;
        this.dblPrice = dblPrice;
        this.strMenu = strMenu;
        this.intEdit = intEdit;
        this.intDelete = intDelete;
        this.intVisibility = intVisibility;
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

    public int getIntEdit() {
        return intEdit;
    }

    public void setIntEdit(int intEdit) {
        this.intEdit = intEdit;
    }

    public int getIntDelete() {
        return intDelete;
    }

    public void setIntDelete(int intDelete) {
        this.intDelete = intDelete;
    }

    public int getIntVisibility() {
        return intVisibility;
    }

    public void setIntVisibility(int intVisibility) {
        this.intVisibility = intVisibility;
    }

    public void EditPriceNMenu(Double price, String Menu){
        strMenu = Menu;
        dblPrice = price;
    }
}