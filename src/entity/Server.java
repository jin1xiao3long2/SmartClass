package entity;

public class Server {
    public String HOST = null;
    public String PORT = null;
    public String USERNAME = null;
    public String PASSWORD = null;
    public String HTTP_ROOT = null;

    public String getHTTP_ROOT() {
        return HTTP_ROOT;
    }

    public void setHTTP_ROOT(String HTTP_ROOT) {
        this.HTTP_ROOT = HTTP_ROOT;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public Server(String host, String port, String username, String password, String path) {
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
        this.HTTP_ROOT = path;
    }

    public Server() {
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
