package entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SysWallpaperFile {

    /**
     * server 储存服务器端信息
     * imgs 作为服务器端数据的缓存
     * map 储存映射表
     */

    private Server server = null;
    private List<SysWallpaperImg> imgs = new ArrayList<>();
    private Map<User, SysWallpaperImg> map = new HashMap<>();

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Map<User, SysWallpaperImg> getMap() {
        return map;
    }

    public void setMap(Map<User, SysWallpaperImg> map) {
        this.map = map;
    }

    public List<SysWallpaperImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<SysWallpaperImg> imgs) {
        this.imgs = imgs;
    }

    public void addClassInfo(User user, SysWallpaperImg img){
        map.put(user, img);
    }
    public SysWallpaperImg delClassInfo(User user){
        return map.remove(user);
    }
}
