package entity;

public class User {

    /**
     * 教室信息
     * wallpaper?可删
     * registry用于修改壁纸
     * classInfo 用于搜索时匹配//可拓展为成员
     */

    private String registry = null;
    private Integer id = null;
    private String name = null;
    private String classInfo = null;

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }
}
