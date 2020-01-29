package SingleItem;

public class MenuItem {
    private String strPrice;
    private String strMenu;
    private int intEdit;
    private int intDelete;

    public MenuItem(String strPrice, String strMenu, int intEdit, int intDelete) {
        this.strPrice = strPrice;
        this.strMenu = strMenu;
        this.intEdit = intEdit;
        this.intDelete = intDelete;
    }

    public String getStrPrice() {
        return strPrice;
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
}