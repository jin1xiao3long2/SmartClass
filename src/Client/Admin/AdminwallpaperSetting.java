package Client.Admin;

import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.FileBaseUtil;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import Client.DownPic.utils.BaseUtils.SftpBaseUtil;
import com.jcraft.jsch.ChannelSftp;
import entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;


import java.util.*;
import java.util.regex.Pattern;

public class AdminwallpaperSetting {


    Server server = null;
    AdminFile adminFile = null; //仅在修改server时可以对该储存的server进行操作, 否则统一使用该类内部的server
    String configRootPath = null;
    String configFilePath = null;
    List<ServerImg> imgs = new ArrayList<>(); //切换为String
    ServerImg defaultImg = null;
    ServerFile serverFile = null;

    //配置文件获取这两个信息
    public void init(Admin admin, RequestFinishListener listener) throws Exception {
//        admin.init and check
        if (admin == null) {
            listener.error(new Exception("Null AdminInfo"));
            throw new Exception("Null AdminInfo");//
        }

        configRootPath = admin.getProjectPath();
        adminFile = admin.getAdminFile();

        if (!checkFile(configRootPath)) {
//            弹窗要求检测并退出
            listener.error(new Exception("Wrong path"));
            throw new Exception("Wrong path");
        }

        configFilePath = Fileinit(configRootPath);
//            JsonBaseUtil.ObjtoSting(adminFile);

//        log init
        LogBaseUtil.init(configRootPath);
//        listener.log("init success, " + JsonBaseUtil.ObjtoSting(adminFile)); //to be delete

    }


    public boolean addUser(User user, String img) {
//        修改adminFile数据
        List<User> Users = adminFile.getUSERS();
        Map<String, String> nimap = adminFile.getNimap();
        if (Users.contains((User) user)) {
//            listener.error(new Exception("添加重复教室"));
            return false;
        }
        Users.add(user);
        nimap.put(user.getName(), img);
//        listener.log("添加" + user.getName() + "成功");
        adminFile.setUSERS(Users);
        adminFile.setNimap(nimap);

        Map<String, String> uimap = serverFile.getUimap();
        uimap.put(user.getUrl(), img);

        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
//        同步数据
        updateLocalFile();
        return true;
    }

    public boolean delUser(User user) {
//        修改adminFile信息
        List<User> Users = adminFile.getUSERS();
        Map<String, String> nimap = adminFile.getNimap();
        if (!Users.contains((User) user)) {
//            listener.error(new Exception("删除未知教室"));
            return false;
        }
        Users.remove((User) user);
        nimap.remove((String) user.getName());
//        listener.log("删除" + user.getName() + "成功");
        adminFile.setUSERS(Users);
        adminFile.setNimap(nimap);

        Map<String, String> uimap = serverFile.getUimap();
        uimap.remove((String) user.getUrl());
        serverFile.setUimap(uimap);
//        同步数据
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);

        //local
        updateLocalFile();


        return true;
    }




    //更改<user, img>缓存?
    public void changeWallpaper(User user, ServerImg img) {
        Map<String, String> nimap = this.adminFile.getNimap();
        Map<String, String> uimap = serverFile.getUimap();
        nimap.put(user.getName(), img.getUrl());
        uimap.put(user.getUrl(), img.getUrl());
        this.adminFile.setNimap(nimap);
        this.serverFile.setUimap(uimap);

//        update
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
        updateLocalFile();
    }


    //    设置服务器相关
    public void setServer(Server server) {
        this.server = server;
        this.adminFile.setSERVER(server);
        SftpBaseUtil.setServerData(server);
        updateLocalFile();
    }

    public void initServer() {
        serverFile = new ServerFile();
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
    }


    public Server getServer() {
        return server;
    }


    public void setUser_root(String user_root) {
        this.adminFile.setUSER_ROOT(user_root);
        updateLocalFile();
    }

    public String getUser_root(){
        return this.adminFile.getUSER_ROOT();
    }

    //设置命名规范相关(可去除)
    public void setName_Base(String name_base) {
        this.adminFile.setNameBase(name_base);
        updateLocalFile();
    }

    public String getName_Base() {
        return this.adminFile.getNameBase();
    }

//    获取图片列表
    public void getImgs() {
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        List<String> fileList = SftpBaseUtil.check(server.HTTP_ROOT, sftp);
//        System.out.println(fileList);

        Iterator iter = fileList.iterator();
        byte[] data = new byte[0];
        while (iter.hasNext()) {
            String filename = (String) iter.next();
            if(filename.length() <= adminFile.getNameBase().length())
                continue;
            if(filename.substring(0, adminFile.getNameBase().length()).equals((String) adminFile.getNameBase())) {
                try {
                    data = SftpBaseUtil.downloadBybyte(server.HTTP_ROOT, filename, filename, sftp);
                }catch (Exception e){
                    e.printStackTrace();
                }
                ServerImg img = new ServerImg();
                img.setData(data);
                img.setUrl(filename);
                imgs.add(img);
            }
        }
        SftpBaseUtil.disconnect(sftp);
        System.out.println("List is : " + JsonBaseUtil.ObjtoSting(imgs));
    }


    /**
     * 上传图片
     *
     * @param listener 响应器
     * @param imgPath  上传路径
     */


    //    wait to be update
    public void uploadPic(String imgPath) {
//        sftp

//        get img list
        List<String> imgName = new ArrayList<>(); //get img1.jpg..  img2.jpg xxx
        Iterator iter = imgs.iterator();
        String suffix = new String();
        while (iter.hasNext()) {
            ServerImg img = (ServerImg) iter.next();
            String name = img.getUrl();
            imgName.add(name);
        }

//        check suffix
        int index = imgPath.indexOf('.');
        suffix = imgPath.substring(index, imgPath.length());

//        get id
        int id = GetId(imgName); //[img1.jpg, img2.jpg , xxx]
        System.out.println(id);
        String uploadFilename = adminFile.getNameBase() + id + suffix;
        ServerImg img = new ServerImg();

        img.setUrl(uploadFilename);//to be finished
        byte[] data = FileBaseUtil.getBytesByFile(imgPath);
        img.setData(data);

//        fstp operation
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        if (SftpBaseUtil.upload(adminFile.getSERVER().getHTTP_ROOT(), imgPath, uploadFilename, sftp)) {
            imgs.add(img);
            //if add listener
        }
        SftpBaseUtil.disconnect(sftp);
        //add error
    }

    /**
     * 删除图片 待完成-> 默认图片的相关处理
     *
     * @param id
     */
    public void delPic(ServerImg img) {

//        check map
        String name = img.getUrl(); //to be finished
        String defaultImgName = defaultImg.getUrl(); //to be finished

        if (imgs.contains((ServerImg) img)) {
            imgs.remove((ServerImg) img);
        }


//        change map with default img
        Map<String, String> nimap = adminFile.getNimap();
        Iterator iter = nimap.values().iterator();
        while (iter.hasNext()) {
            String nameInfo = (String) iter.next();
            if (nameInfo.equals(name)) {
                if(defaultImgName != null)
                    nameInfo = defaultImgName;
            }
        }
        adminFile.setNimap(nimap);

//        update local
        updateLocalFile();
//        update server
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
    }

//    减少使用并作为缓存记录
    public Map<String,ServerImg> getMap(){
        Map<String, ServerImg> nimap = new HashMap<>();
        Iterator iter = adminFile.getNimap().entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry)iter.next();
            Iterator imgIter = imgs.iterator();
            while(imgIter.hasNext()){
                ServerImg img =(ServerImg) imgIter.next();
                String url = (img).getUrl();
                if(entry.getValue().equals((String)url)){
                    nimap.put(entry.getKey(), img);
                }
            }
        }
        return nimap;
    }

    public void setDefaultImg(ServerImg img){
        defaultImg = img;
        this.adminFile.setDefaultImgurl(defaultImg.getUrl());
        updateLocalFile();
    }

    //    管理员文件(configRootPath\wallpaper\settings.json)init
    private String Fileinit(String projectPath) {
        String wallpaperPath = projectPath + "\\wallpaper";
        File wallpaperDir = new File(wallpaperPath);
        if (!wallpaperDir.exists())
            wallpaperDir.mkdir();
        String settingPath = wallpaperPath + "\\settings.json";
        File settingFile = new File(settingPath);
        if (!settingFile.exists()) {
            try {
                settingFile.createNewFile();
                adminFile = new AdminFile();
                adminFile.setUSER_ROOT(null);
                List<User> users = new ArrayList<>();
                adminFile.setUSERS(users);
                adminFile.setSERVER(server);
                Map<String, String> nimap = new HashMap<>();
                adminFile.setNimap(nimap);
                adminFile.setNameBase(null);
                adminFile.setDefaultImgurl(null);
                FileBaseUtil.writeFile(settingPath, JsonBaseUtil.ObjtoSting(adminFile));
                //若不存在,则创建setting文件并写入默认AdminFile信息
            } catch (Exception e) {
                e.printStackTrace();
//                异常放外面处理
            }
        } else {

            String adminFileText = FileBaseUtil.readFile(settingPath);
            JSONObject adminFileObj = JSONObject.fromObject(adminFileText);
            System.out.println(adminFileObj.toString());
            Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
            classMap.put("USERS", User.class);
            this.adminFile = (AdminFile) JSONObject.toBean(adminFileObj, AdminFile.class, classMap);
            JSONArray arr = adminFileObj.getJSONArray("nimap");
            if(arr.size() == 1)
                return settingPath;
//            Map<String, String> nimap = JsonBaseUtil.parseJSON2Map(adminFileObj.getJSONArray("nimap"));
            Map<String, String> nimap = new HashMap<>();
            for(Object obj: arr){
                Iterator iter = ((JSONObject) obj).keys();
                if(iter.hasNext()){
                    String key = (String) iter.next();
                    String value = ((JSONObject) obj).getString(key);
                    nimap.put(key, value);
                }
            }
            adminFile.setNimap(nimap);

            //若存在,则主机同步至程序(adminFile)
//            文件损坏另外处理!!
        }
        return settingPath;
    }




    private boolean checkFile(String projectPath) {
        File file = new File(projectPath);
        if (!file.exists())
            return false;
        return true;
    }


    //imgList and defaultImg


    private void updateServerFile(ChannelSftp sftp) {
//        get file info
        Map<String, String> nimap = adminFile.getNimap();
        List<User> users = adminFile.getUSERS();
        Map<String, String> uimap = new HashMap<>();
        Iterator iter = nimap.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            Iterator userIter = users.iterator();
            while(userIter.hasNext()){
                User user = (User) userIter.next();
                if(user.getName().equals((String)entry.getKey())){
                    uimap.put(user.getUrl(), entry.getValue());
                }
            }
        }

        serverFile.setUimap(uimap);
//        serverFile
//        create new file and write

        String tempFileName = FileBaseUtil.createFile(configRootPath + "\\wallpaper\\tempsettingFile.json");

        System.out.println("tempFile is : " + tempFileName);
        FileBaseUtil.writeFile(tempFileName, JsonBaseUtil.ObjtoSting(serverFile));
//        sftp operate

        List<String> fileList = SftpBaseUtil.check(adminFile.getSERVER().getHTTP_ROOT(), sftp);
        if (fileList.contains((String) "settings.json")) {
            SftpBaseUtil.delete(server.HTTP_ROOT, "settings.json", sftp);
        }
        SftpBaseUtil.upload(server.HTTP_ROOT, tempFileName, "settings.json", sftp);

//        del tempfile
        FileBaseUtil.deleteFile(tempFileName);
    }

    private void updateLocalFile() {
//        localFile
        FileBaseUtil.writeFile(configFilePath, JsonBaseUtil.ObjtoSting(adminFile));
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
