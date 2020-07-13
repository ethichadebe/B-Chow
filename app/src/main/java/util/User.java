package util;

import com.google.android.gms.maps.model.LatLng;

import static util.Constants.getIpAddress;

public class User {
    private int uID, uType;
    private String uName, uSurname, uSex, uEmail, uNumber, uPicture, uPassword, uAddress;
    private LatLng uLocation;

    /**
     * @param uID        user database ID
     * @param uName      user name
     * @param uSurname   user surname
     * @param uSex       user gender
     * @param uEmail     user email
     * @param uNumber    user phone number
     * @param uType      user Type(Beanery owner, employee, customer)
     * @param uPassword  user Password
     * @param uAddress   user saved address
     * @param uLocation user Coordinates
     */
    public User(int uID, String uName, String uSurname, String uAddress, LatLng uLocation, String uSex, String uEmail, String uNumber,
                int uType, String uPassword) {
        this.uID = uID;
        this.uName = uName;
        this.uSurname = uSurname;
        this.uSex = uSex;
        this.uAddress = uAddress;
        this.uLocation = uLocation;
        this.uEmail = uEmail;
        this.uNumber = uNumber;
        this.uType = uType;
        this.uPassword = uPassword;
    }

    /**
     * @param uID        user database ID
     * @param uName      user name
     * @param uSurname   user surname
     * @param uSex       user gender
     * @param uEmail     user email
     * @param uNumber    user phone number
     * @param uPicture   user profile picture
     * @param uType      user Type(Beanery owner, employee, customer)
     * @param uAddress   user saved address
     * @param uLocation user Coordinates
     */
    public User(int uID, String uName, String uSurname, String uAddress, LatLng uLocation, String uSex, String uEmail, String uNumber,
                String uPicture, int uType) {
        this.uID = uID;
        this.uName = uName;
        this.uSurname = uSurname;
        this.uSex = uSex;
        this.uAddress = uAddress;
        this.uLocation = uLocation;
        this.uEmail = uEmail;
        this.uNumber = uNumber;
        this.uType = uType;
        this.uPicture = uPicture;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuSurname() {
        return uSurname;
    }

    public void setuSurname(String uSurname) {
        this.uSurname = uSurname;
    }

    public String getuSex() {
        return uSex;
    }

    public void setuSex(String uSex) {
        this.uSex = uSex;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuNumber() {
        return uNumber;
    }

    public void setuNumber(String uNumber) {
        this.uNumber = uNumber;
    }

    public int getuType() {
        return uType;
    }

    public void setuType(int uType) {
        this.uType = uType;
    }

    public String getuPicture() {
        return uPicture;
    }

    public void setuPicture(String uPicture) {
        this.uPicture = uPicture;
    }

    public String getuPassword() {
        return uPassword;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public LatLng getuLocation() {
        return uLocation;
    }

    public void setuLocation(LatLng uLocation) {
        this.uLocation = uLocation;
    }
}
