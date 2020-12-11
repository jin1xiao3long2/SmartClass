package entity;

public class SysWallpaperImg {

    /**
     * id唯一匹配信息
     * filename作为搜索信息
     */
    private int id;
    private String filename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
