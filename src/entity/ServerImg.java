package entity;

public class ServerImg {

    /**
     * id唯一匹配信息
     * filename作为搜索信息
     */
    private int id;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
