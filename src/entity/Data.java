package entity;

public class Data {
    public String server_host;
    public String server_port;
    public String server_username;

    public String getServer_host() {
        return server_host;
    }

    public void setServer_host(String server_host) {
        this.server_host = server_host;
    }

    public String getServer_port() {
        return server_port;
    }

    public void setServer_port(String server_port) {
        this.server_port = server_port;
    }

    public Data(String server_host, String server_port, String server_username, String server_password, String server_http_root, String user_root, String name_base) {
        this.server_host = server_host;
        this.server_port = server_port;
        this.server_username = server_username;
        this.server_password = server_password;
        this.server_http_root = server_http_root;
        this.user_root = user_root;
        this.name_base = name_base;
    }

    public String getServer_username() {
        return server_username;
    }

    public void setServer_username(String server_username) {
        this.server_username = server_username;
    }

    public String getServer_password() {
        return server_password;
    }

    public void setServer_password(String server_password) {
        this.server_password = server_password;
    }

    public String getServer_http_root() {
        return server_http_root;
    }

    public void setServer_http_root(String server_http_root) {
        this.server_http_root = server_http_root;
    }

    public String getUser_root() {
        return user_root;
    }

    public void setUser_root(String user_root) {
        this.user_root = user_root;
    }

    public String getName_base() {
        return name_base;
    }

    public void setName_base(String name_base) {
        this.name_base = name_base;
    }

    public String server_password;
    public String server_http_root;
    public String user_root;
    public String name_base;
}
