package entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServerFile {
    /**
     * Server的映射表文件
     */

    private Map<User, ServerImg> maps = null;

    public Map<User, ServerImg> getMaps() {
        return maps;
    }

    public void setMaps(Map<User, ServerImg> maps) {
        this.maps = maps;
    }
}
