package zrkc.group.javabean;


import java.util.Map;


public class ServerFile {
    /**
     * Server的映射表文件
     */

    private Map<String, String> uimap = null;

    public Map<String, String> getUimap() {
        return uimap;
    }

    public void setUimap(Map<String, String> uimap) {
        this.uimap = uimap;
    }

    @Override
    public String toString() {
        return "ServerFile{" +
                "uimap=" + uimap +
                '}';
    }
}
