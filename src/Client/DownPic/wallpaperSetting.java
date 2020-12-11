package Client.DownPic;

import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import Client.DownPic.utils.BaseUtils.SftpBaseUtil;
import Client.DownPic.utils.JsonFileUtil;
import Client.ui.Change.change;
import com.jcraft.jsch.ChannelSftp;
import entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class wallpaperSetting {

    private String projectPath = new String();
    private String imgsPath = new String();
    private SysWallpaperFile writeFile = new SysWallpaperFile();
    private final String nameBase = "xxx";
    private MainUser mainUser = new MainUser();

    public boolean init(entity.Server server, MainUser mainUser) {
        //find path
        projectPath = mainUser.getFilepath();
        this.mainUser = mainUser;
        //create dirs
        String wallpaperDir = projectPath + "\\wallpaper";
        File wallpaperDirFile = new File(wallpaperDir);
        if(!wallpaperDirFile.exists())
            wallpaperDirFile.mkdir();
        String imgsDir = wallpaperDir + "\\imgs";
        imgsPath = imgsDir;
        File imgsDirFile = new File(imgsDir);
        if(!imgsDirFile.exists())
            imgsDirFile.mkdir();
//        create file
        String settingsFilename = wallpaperDir + "\\settings.json";
        File settingsFile = new File(settingsFilename);
        if(!settingsFile.exists()){
            try{
                settingsFile.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //write file
        writeFile.setServer(server);
        List<SysWallpaperImg> imgs = new ArrayList<>();
        writeFile.setImgs(imgs);
        Map<entity.User, SysWallpaperImg> map = new HashMap<>();
        writeFile.setMap(map);
        String text = JsonBaseUtil.ObjtoSting(writeFile);
        JsonFileUtil.changeJsonInfo(wallpaperDir, text);
        //
        return true;
    }

    /**
     * @param id   用于数据库拓展
     * @param path 路径
     * @param name 图片名
     * @param url  路径
     * @return
     */
    public boolean uploadPic(String uploadFilename) {
        //get info buffer
        //get fileList
        String settingFilePath = mainUser.getFilepath() + "\\wallpaper";

        JSONObject settingObj = JSONObject.fromObject(JsonFileUtil.getJsonInfo(projectPath + "\\wallpaper"));
        JSONArray imgs = settingObj.getJSONArray("imgs");
        int length = imgs.size();
        List<String> fileList = new ArrayList<>();
        for(int i = 0; i < length; i++){
            JSONObject obj = imgs.getJSONObject(i);
            String filename = obj.getString("filename");
            if(filename != null)
                fileList.add(filename);
        }

        //check backwords
        int index = uploadFilename.indexOf('.');
        String backwords = uploadFilename.substring(index, uploadFilename.length());

        //checkname
        int fileid =  checkName(fileList);
        String uploadName = nameBase + fileid + backwords;
        SysWallpaperImg img = new SysWallpaperImg();
        img.setId(fileid);
        img.setFilename(uploadName);

        //sftp.add
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        if(SftpBaseUtil.upload(((JSONObject)settingObj.get("server")).getString("path"), uploadFilename, uploadName, sftp)){
            ;
            //listener
        }else{
            //listener
            return false;
        }
        SftpBaseUtil.disconnect(sftp);

        //file.add(pic)
        try{
            JsonBaseUtil.addDataInArr(settingObj, "imgs", (Object)img);
            JsonFileUtil.changeJsonInfo(settingFilePath, settingObj.toString());
        }catch (Exception e){
            e.printStackTrace();
            //listener
            return false;
        }
        return true;
    }

    /**
     * @param id 储存图片的id
     * @return
     */

    public boolean delPic(int id) {
        //settings.del(pic)
        //file.del(pic)
        return true;
    }


    /**
     *
     * @param user 教室
     * @param id   图片信息
     * @return
     */
    public boolean changePic(User user, int id) {
//        String img = imgPath + "\\" + id + ".jpg";
        String img = null;
        change.change(img);
        return true;
    }


    private int checkName(List<String> nameList){
        List<Integer> id = new ArrayList<>();
        //将结尾的数字导入至数组
        for(int i = 0; i < nameList.size(); i++){
            String value = delStr(nameList.get(i));
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if(pattern.matcher(value).matches()){
                int val = Integer.parseInt(value);
                id.add(val);
            }
        }

        Arrays.sort(new List[]{id});
        Iterator iter = id.iterator();
        Integer first = 0;
        while(iter.hasNext()){
            Integer num = (Integer) iter.next();
            if(num - first > 1){
                return (first + 1);
            }
            first = num;
        }
        return first + 1;
    }


    private String delStr(String str){
        StringBuffer sb = new StringBuffer(str);
        int index = sb.indexOf(nameBase);
        if(index == -1){
            return "0";
        }
        sb.delete(index, index + nameBase.length());
        index = sb.indexOf(".");
        String id = sb.substring(0, index);
        return id;
    }

}