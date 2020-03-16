package util;

public class Constants {
    private static final String IP_ADDRESS = "192.168.43.244";
    private static final String PORT = "3005";

    public static String getIpAddress() {
        return IP_ADDRESS+":"+PORT;
    }
}
