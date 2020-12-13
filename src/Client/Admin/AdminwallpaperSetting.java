package Client.Admin;

import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.FileBaseUtil;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import Client.DownPic.utils.BaseUtils.SftpBaseUtil;
import com.jcraft.jsch.ChannelSftp;
import entity.*;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminwallpaperSetting {

    Admin admin = null;
    Server server = null;
    AdminFile adminFile = null;

    //配置文件获取这两个信息
    public void init(String projectPath, Server server, String USER_ROOT){
//        admin.init and check
        admin = new Admin();
        admin.setProjectPath(projectPath);
        if(!checkFile(projectPath))
        {
//            弹窗要求检测并退出
        }
//        server.init
        this.server = server;
//        sftp.init
        SftpBaseUtil.setServerData(server);
        
//        ChannelSftp sftp = SftpBaseUtil.getConnectIP(new RequestFinishListener() {
//            @Override
//            public void log(String response) {
//                //前端显示连接username成功
//            }
//
//            @Override
//            public void error(Exception ex) {
//                //前端显示连接失败并退出
//            }
//        });

//        SftpBaseUtil.disconnect(sftp);
//        check server connect

//        http.init
//        check server connect
        //同sftp init

//        serverfile.init and check
        Fileinit(projectPath, USER_ROOT);

//        log init
        LogBaseUtil.init(projectPath);
    }



    public void addUser(){

    }

    public void DelUser(){

    }

    public void UploadPic(){

    }

    public void DelPic(){

    }

    public void ChangePic(){

    }

    public void getImg(){

    }

    private void Fileinit(String projectPath, String URL_ROOT){
        String wallpaperPath = projectPath + "\\wallpaper";
        File wallpaperDir = new File(wallpaperPath);
        if(!wallpaperDir.exists())
            wallpaperDir.mkdir();
        String settingPath = wallpaperPath + "\\settings.json";
        File settingFile = new File(settingPath);
        if(!settingFile.exists()){
            try{
                settingFile.createNewFile();
                adminFile = new AdminFile();
                adminFile.setSERVER(server);
                List<User> users = new ArrayList<>();
                adminFile.setUSERS(users);
                Map<User, ServerImg> map = new HashMap<>();
                adminFile.setMaps(map);
                adminFile.setURL_ROOT(URL_ROOT);
                FileBaseUtil.writeFile(settingPath, JsonBaseUtil.ObjtoSting(adminFile));
                //若不存在,则新建adminFile并同步至主机
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            String adminFileText = FileBaseUtil.ReadFile(settingPath);
            JSONObject adminFileObj = JSONObject.fromObject(adminFileText);
            this.adminFile = (AdminFile) JSONObject.toBean(adminFileObj, AdminFile.class);
                //若存在,则主机同步至程序(adminFile)
        }
    }

    private boolean checkFile(String projectPath){
        File file = new File(projectPath);
        if(!file.exists())
            return false;
        return true;
    }

}
