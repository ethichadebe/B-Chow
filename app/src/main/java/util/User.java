package util;

public class User {
    private int uID;
    private String uName;
    private String uSurname;
    private String uDOB;
    private String uSex;
    private String uEmail;
    private String uNumber;
    private String uPassword;
    private int uType;


    /**
     *
     * @param uID
     * @param uName
     * @param uSurname
     * @param uDOB
     * @param uSex
     * @param uEmail
     * @param uNumber
     * @param uType
     */
    public User(int uID, String uName, String uSurname, String uDOB, String uSex, String uEmail, String uNumber,
                int uType, String uPassword) {
        this.uID = uID;
        this.uName = uName;
        this.uSurname = uSurname;
        this.uDOB = uDOB;
        this.uSex = uSex;
        this.uEmail = uEmail;
        this.uNumber = uNumber;
        this.uType = uType;
        this.uPassword = uPassword;
    }

    /**
     *
     * @param uID
     * @param uName
     * @param uSurname
     * @param uDOB
     * @param uSex
     * @param uEmail
     * @param uNumber
     * @param uType
     */
    public User(int uID, String uName, String uSurname, String uDOB, String uSex, String uEmail, String uNumber,
                int uType) {
        this.uID = uID;
        this.uName = uName;
        this.uSurname = uSurname;
        this.uDOB = uDOB;
        this.uSex = uSex;
        this.uEmail = uEmail;
        this.uNumber = uNumber;
        this.uType = uType;
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

    public String getuDOB() {
        return uDOB;
    }

    public void setuDOB(String uDOB) {
        this.uDOB = uDOB;
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

    public int getAdmin() {
        return uType;
    }

    public void setAdmin(int admin) {
        uType = admin;
    }

    public String getuPassword() {
        return uPassword;
    }

    public int getuType() {
        return uType;
    }

    public void setuType(int uType) {
        this.uType = uType;
    }
}
