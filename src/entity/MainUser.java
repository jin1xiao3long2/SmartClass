package entity;

import java.util.List;
import java.util.Map;


public class MainUser {

    /**
     * 管理员
     * users储存各教室信息// 可拓展为Main->campus->buildings->class
     * imgs储存图片信息
     * map储存映射表
     * filepath 储存当前主机储存信息
     */

    private List<User> users = null;
    private String filepath = null;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
