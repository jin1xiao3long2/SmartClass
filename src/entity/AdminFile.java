package entity;

import java.util.List;
import java.util.Map;


public class AdminFile {

    /**
     * Server 储存SERVER信息
     * URL_ROOT 储存user的root
     * USERS 储存user的url与name
     * Map 储存user-img映射表
     */

    private Server SERVER = null;
    private String USER_ROOT = null;
    private List<User> USERS = null;
    private Map<String, String> nimap = null;// name to imgName
    private String nameBase = null;
    private String defaultImgurl = null;

    public String getDefaultImgurl() {
        return defaultImgurl;
    }

    public void setDefaultImgurl(String defaultImgurl) {
        this.defaultImgurl = defaultImgurl;
    }

    public String getNameBase() {
        return nameBase;
    }

    public void setNameBase(String nameBase) {
        this.nameBase = nameBase;
    }

    public Server getSERVER() {
        return SERVER;
    }

    public void setSERVER(Server SERVER) {
        this.SERVER = SERVER;
    }

    public String getUSER_ROOT() {
        return USER_ROOT;
    }

    public void setUSER_ROOT(String USER_ROOT) {
        this.USER_ROOT = USER_ROOT;
    }

    public List<User> getUSERS() {
        return USERS;
    }

    public void setUSERS(List<User> USERS) {
        this.USERS = USERS;
    }

    public Map<String, String> getNimap() {
        return nimap;
    }

    public void setNimap(Map<String, String> nimap) {
        this.nimap = nimap;
    }

}
