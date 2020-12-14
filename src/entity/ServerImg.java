package entity;

public class ServerImg {

    /**
     * id唯一匹配信息
     * filename作为搜索信息
     */
    private byte[] data;
    private String url;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
