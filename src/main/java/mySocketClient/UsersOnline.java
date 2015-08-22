package mySocketClient;

import java.io.Serializable;

/**
 * Created by Admin on 02.04.15.
 */
public class UsersOnline implements Serializable{
    private String port;
    private String uName;
    public UsersOnline(String port, String uName){
        this.port = port;
        this.uName = uName;
    }

    public String getuName() {
        return uName;
    }

    public String getPort() {
        return port;
    }

}
