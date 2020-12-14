package Client.Admin;

import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.FileBaseUtil;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import Client.DownPic.utils.BaseUtils.SftpBaseUtil;
import Client.DownPic.utils.JsonFileUtil;
import com.jcraft.jsch.ChannelSftp;
import entity.*;
import net.sf.json.JSONObject;

import java.io.*;


import java.util.*;
import java.util.regex.Pattern;

public class AdminwallpaperSetting {

    Admin admin = null;
    Server server = null;
    AdminFile adminFile = null;
    String settingPath = null;
    List<ServerImg> imgs = null;
    ServerImg defaultImg = null;

    //配置文件获取这两个信息
    public void init(String projectPath, Server server, String USER_ROOT, String NameBase) {
//        admin.init and check
        admin = new Admin();
        admin.setProjectPath(projectPath);
        if (!checkFile(projectPath)) {
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
        Fileinit(projectPath, USER_ROOT, NameBase);

//        log init
        LogBaseUtil.init(projectPath);
    }



    public boolean addUser(User user, String img, RequestFinishListener listener) {
//        修改adminFile数据
        List<User> Users = adminFile.getUSERS();
        Map<User, String> map = adminFile.getMap();
        if (Users.contains((User) user)) {
            listener.error(new Exception("添加重复教室"));
            return false;
        }
        Users.add(user);
        map.put(user, img);
        listener.log("添加" + user.getName() + "成功");
        adminFile.setUSERS(Users);
        adminFile.setMap(map);
//        同步数据
        updateSettingFile(listener);
        return true;
    }

    public boolean DelUser(User user, RequestFinishListener listener) {
//        修改adminFile信息
        List<User> Users = adminFile.getUSERS();
        Map<User, String> map = adminFile.getMap();
        if (!Users.contains((User) user)) {
            listener.error(new Exception("删除未知教室"));
            return false;
        }
        Users.remove((User) user);
        map.remove((User) user);
        listener.log("删除" + user.getName() + "成功");
        adminFile.setUSERS(Users);
        adminFile.setMap(map);
//        同步数据
        updateSettingFile(listener);
        return true;
    }

    /**
     * 上传图片
     *
     * @param listener 响应器
     * @param imgPath  上传路径
     */

    public void UploadPic(RequestFinishListener listener, String imgPath) {
//        sftp

//        get img list
        List<String> imgName = new ArrayList<>();
        Iterator iter = imgs.iterator();
        while (iter.hasNext()) {
            ServerImg img = (ServerImg) iter.next();
            String Fullname = img.getUrl();
            int index = Fullname.indexOf('.');
            String name = Fullname.substring(0, index);
            imgName.add(name);
        }

//        check suffix
        int index = imgPath.indexOf('.');
        String suffix = imgPath.substring(index - 1, imgPath.length());

//        get id
        int id = GetId(imgName);
        String uploadFilename = adminFile.getNameBase() + id + suffix;
        ServerImg img = new ServerImg();
        img.setId(id);
        img.setUrl(uploadFilename);//to be finished


//        fstp operation
        ChannelSftp sftp = SftpBaseUtil.getConnectIP(listener);
        if (SftpBaseUtil.upload(adminFile.getSERVER().getHTTP_ROOT(), imgPath, uploadFilename, sftp)) {
            imgs.add(img);
            //if add listener
        }
        SftpBaseUtil.disconnect(sftp);
        //add error
    }

    /**
     * 删除图片
     *
     * @param id
     */
    public void DelPic(ServerImg img, RequestFinishListener listener) {

//        check map
        String name = img.getUrl(); //to be finished
        String defaultImgName = defaultImg.getUrl(); //to be finished

        if(imgs.contains((ServerImg) img)){
            imgs.remove((ServerImg) img);
        }


//        change map with default img
        Map<User, String> map = adminFile.getMap();
        Iterator iter = map.values().iterator();
        while(iter.hasNext()){
            String nameInfo = (String)iter.next();
            if(nameInfo.equals(name))
                nameInfo = defaultImgName;
        }
        adminFile.setMap(map);

//        update
        updateSettingFile(listener);

    }

//
    public void ChangeWallpaper(User uer, ServerImg img) {
//
    }

    public void ChangeDefaultPic(ServerImg oldImg, ServerImg newImg){

    }

    public void getImg() {

    }

    //    管理员文件(projectPath\wallpaper\settings.json)init
    private void Fileinit(String projectPath, String URL_ROOT, String NameBase) {
        String wallpaperPath = projectPath + "\\wallpaper";
        File wallpaperDir = new File(wallpaperPath);
        if (!wallpaperDir.exists())
            wallpaperDir.mkdir();
        String settingPath = wallpaperPath + "\\settings.json";
        this.settingPath = settingPath;
        File settingFile = new File(settingPath);
        if (!settingFile.exists()) {
            try {
                settingFile.createNewFile();
                adminFile = new AdminFile();
                adminFile.setSERVER(server);
                List<User> users = new ArrayList<>();
                adminFile.setUSERS(users);
                Map<User, String> map = new HashMap<>();
                adminFile.setMap(map);
                adminFile.setUSER_ROOT(URL_ROOT);
                adminFile.setNameBase(NameBase);
                FileBaseUtil.writeFile(settingPath, JsonBaseUtil.ObjtoSting(adminFile));
                //若不存在,则创建setting文件并写入默认AdminFile信息
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String adminFileText = FileBaseUtil.readFile(settingPath);
            JSONObject adminFileObj = JSONObject.fromObject(adminFileText);
            this.adminFile = (AdminFile) JSONObject.toBean(adminFileObj, AdminFile.class);
            //若存在,则主机同步至程序(adminFile)
        }
    }

    private boolean checkFile(String projectPath) {
        File file = new File(projectPath);
        if (!file.exists())
            return false;
        return true;
    }



    //imgList and defaultImg

    private void initImgs() {
//
    }

    private void updateSettingFile(RequestFinishListener listener) {
//        get file info
        Map<User, String> map = adminFile.getMap();
//        localFile
        JsonFileUtil.changeJsonInfo(settingPath, JsonBaseUtil.ObjtoSting(adminFile));
//        serverFile
//        create new file and write
        String tempFileName = FileBaseUtil.createFile(admin.getProjectPath() + "\\tempsettingFile.json");
        JsonFileUtil.changeJsonInfo(tempFileName, JsonBaseUtil.ObjtoSting(map));

//        sftp operate
        ChannelSftp sftp = SftpBaseUtil.getConnectIP(listener);
        List<String> fileList = SftpBaseUtil.check(adminFile.getSERVER().getHTTP_ROOT(), sftp);
        if (fileList.contains((String) "settings.json")) {
            SftpBaseUtil.delete(adminFile.getSERVER().getHTTP_ROOT(), "settings.json", sftp, listener);
        }
        SftpBaseUtil.upload(adminFile.getSERVER().getHTTP_ROOT(), tempFileName, "settings,json", sftp);

//        del tempfile
        FileBaseUtil.deleteFile(tempFileName);
    }

    private int GetId(List<String> nameList) {
        List<Integer> id = new ArrayList<>();
        //将结尾的数字导入至数组
        for (int i = 0; i < nameList.size(); i++) {
            String value = delStr(nameList.get(i));
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(value).matches()) {
                int val = Integer.parseInt(value);
                id.add(val);
            }
        }

        Arrays.sort(new List[]{id});
        Iterator iter = id.iterator();
        Integer first = 0;
        while (iter.hasNext()) {
            Integer num = (Integer) iter.next();
            if (num - first > 1) {
                return (first + 1);
            }
            first = num;
        }
        return first + 1;
    }


    private String delStr(String str) {
        StringBuffer sb = new StringBuffer(str);
        int index = sb.indexOf(adminFile.getNameBase());
        if (index == -1) {
            return "0";
        }
        sb.delete(index, index + adminFile.getNameBase().length());
        index = sb.indexOf(".");
        String id = sb.substring(0, index);
        return id;
    }

}
