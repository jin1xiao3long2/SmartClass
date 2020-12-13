package entity;

import java.util.List;
import java.util.Map;


public class AdminFile {

    /**
     *  Server 储存SERVER信息
     *  URL_ROOT 储存user的root
     *  USERS 储存user的url与name
     *  Map 储存user-img映射表
     */

    private Server SERVER = null;
    private String URL_ROOT = null;
    private List<User> USERS = null;
    private Map<User, ServerImg> maps = null;

    public Server getSERVER() {
        return SERVER;
    }

    public void setSERVER(Server SERVER) {
        this.SERVER = SERVER;
    }

    public String getURL_ROOT() {
        return URL_ROOT;
    }

    public void setURL_ROOT(String URL_ROOT) {
        this.URL_ROOT = URL_ROOT;
    }

    public List<User> getUSERS() {
        return USERS;
    }

    public void setUSERS(List<User> USERS) {
        this.USERS = USERS;
    }

    public Map<User, ServerImg> getMaps() {
        return maps;
    }

    public void setMaps(Map<User, ServerImg> maps) {
        this.maps = maps;
    }

    public void MapAdd(User user, ServerImg img){
        maps.put(user, img);
    }

    public void MapRemove(User user){
        maps.remove(user);
    }
}
