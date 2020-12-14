package Client.DownPic.utils;

import Client.DownPic.utils.BaseUtils.FileBaseUtil;

public class JsonFileUtil {

    /**
     * 获取json文件信息
     * @param mainUser
     * @return
     */
    public static String getJsonInfo(String path){
        return FileBaseUtil.readFile(path + "\\settings.json");
    }

    /**
     * 修改json文件信息
     * @param mainUser
     * @param jsonText
     */
    public static void changeJsonInfo(String path, String jsonText){
        FileBaseUtil.writeFile(path + "\\settings.json", jsonText);
    }

}
