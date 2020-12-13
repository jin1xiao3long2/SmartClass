package entity;

public class Server {
    public String Host = null;
    public String Port = null;
    public String Username = null;
    public String Password = null;
    public String url = null;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public Server(String host, String port, String username, String password, String path) {
        Host = host;
        Port = port;
        Username = username;
        Password = password;
        this.url = path;
    }

    public Server() {
    }

    public void setPassword(String password) {
        Password = password;
    }
}
