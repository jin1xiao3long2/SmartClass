package entity;


import java.util.Map;


public class ServerFile {
    /**
     * Server的映射表文件
     */

    private Map<User, String> map = null;

    public Map<User, String> getMap() {
        return map;
    }

    public void setMap(Map<User, String> map) {
        this.map = map;
    }
}
