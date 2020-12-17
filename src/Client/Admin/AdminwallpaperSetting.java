package Client.Admin;

import Client.DownPic.utils.BaseUtils.*;
import com.jcraft.jsch.ChannelSftp;
import entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;


import java.util.*;
import java.util.regex.Pattern;

public class AdminwallpaperSetting {


    Server server = null;
    AdminFile adminFile = new AdminFile(); //仅在修改server时可以对该储存的server进行操作, 否则统一使用该类内部的server
    String configRootPath = null;
    String configFilePath = null;
    List<ServerImg> imgs = new ArrayList<>(); //切换为String
    ServerImg defaultImg = null;
    ServerFile serverFile = new ServerFile();

    //配置文件获取这两个信息

    /**
     * 设置projectRoot and settingFileUrl
     *
     * @param admin
     * @param listener
     * @throws Exception
     */
    public void init(Admin admin) throws Exception {
//        admin.init and check
        String errorInfo = "初始化失败! ";

        if (admin == null) {
//            listener.error(new Exception("Null AdminInfo"));
            throw new Exception(errorInfo + "管理员信息异常");//
        }

        configRootPath = admin.getProjectPath();
        adminFile = admin.getAdminFile();

        checkFile(configRootPath);


//      configFilePath include
        configFilePath = initFile(configRootPath);
//            JsonBaseUtil.ObjtoSting(adminFile);

        LogBaseUtil.init(configRootPath);
    }


    public void addUser(User user, String img) throws Exception {
//        修改adminFile数据

//        同步检测


//        数据获取
        String errorInfo = "添加用户失败: ";
        List<User> Users = adminFile.getUSERS();
        Map<String, String> nimap = adminFile.getNimap();
        if (Users.contains((User) user)) {
            throw new Exception(errorInfo + "教室已经存在");
        }


//        数据修改
        Users.add(user);
        nimap.put(user.getName(), img);
        adminFile.setUSERS(Users);
        adminFile.setNimap(nimap);

//        数据同步
        try {
//            server
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            updateServerFile(sftp);
            SftpBaseUtil.disconnect(sftp);
//            local
            updateLocalFile();
        } catch (Exception e) {
            Users.remove(user);
            nimap.remove(user.getName());
            adminFile.setUSERS(Users);
            adminFile.setNimap(nimap);
            throw new Exception(errorInfo + e.getMessage());
        }
    }


//    用户相关

    public void delUser(User user) throws Exception {
//        修改adminFile信息
        String errorInfo = "删除用户异常: ";

//        同步检测

//        数据获取

        List<User> Users = adminFile.getUSERS();
        Map<String, String> nimap = adminFile.getNimap();

        if (!Users.contains((User) user)) {
            throw new Exception(errorInfo + "删除未知用户");
        }

//        数据修改
        Users.remove((User) user);
        nimap.remove((String) user.getName());

        String imgurl = nimap.get(user.getName());

        adminFile.setUSERS(Users);
        adminFile.setNimap(nimap);

        Map<String, String> uimap = serverFile.getUimap();
        uimap.remove((String) user.getUrl());
        serverFile.setUimap(uimap);
//        数据同步
        try {
//            server
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            updateServerFile(sftp);
            SftpBaseUtil.disconnect(sftp);
//            local
            updateLocalFile();
        } catch (Exception e) {
            Users.add(user);
            nimap.put(user.getName(), imgurl);
            adminFile.setUSERS(Users);
            adminFile.setNimap(nimap);
            uimap.put(user.getUrl(), imgurl);
            serverFile.setUimap(uimap);
            throw new Exception(errorInfo + e.getMessage());
        }

    }

    public List<User> getUsers() {
        return adminFile.getUSERS();
    }


    public void changeWallpaper(User user, ServerImg img) throws Exception {
        String errorInfo = "修改壁纸异常: ";
//        同步检测

//        数据获取
        Map<String, String> nimap = this.adminFile.getNimap();
        Map<String, String> uimap = serverFile.getUimap();

//        数据修改
        nimap.put(user.getName(), img.getUrl());
        uimap.put(user.getUrl(), img.getUrl());

        String originName = user.getName();
        String originUrl = user.getUrl();
        String originImgUrl = nimap.get(originName);


        this.adminFile.setNimap(nimap);
        this.serverFile.setUimap(uimap);

//        同步数据
        try {
//            server
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            updateServerFile(sftp);
            SftpBaseUtil.disconnect(sftp);
//            local
            updateLocalFile();
        } catch (Exception e) {
            nimap.put(originName, originImgUrl);
            uimap.put(originUrl, originImgUrl);
            this.adminFile.setNimap(nimap);
            this.serverFile.setUimap(uimap);
            throw new Exception(errorInfo + e.getMessage());
        }
    }


    public Data getData() {
        return new Data(server.HOST, server.PORT, server.USERNAME, server.PASSWORD, server.HTTP_ROOT, adminFile.getUSER_ROOT(), adminFile.getNameBase());
    }

    public void initServerFile() throws Exception {
        String errorInfo = "初始化服务器异常: ";
        Map<String, String> uimap = new HashMap<>();
        serverFile.setUimap(uimap);
        try {
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            updateServerFile(sftp);
            SftpBaseUtil.disconnect(sftp);
        } catch (Exception e) {
            throw new Exception(errorInfo + e.getMessage());
        }
    }


    //    获取图片列表
    public void initImgs() throws Exception {

        String errorInfo = "获取服务器图片列表异常: ";

        try {
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            List<String> fileList = SftpBaseUtil.check(server.HTTP_ROOT, sftp);
//        System.out.println(fileList);

            Iterator iter = fileList.iterator();
            byte[] data = new byte[0];
            while (iter.hasNext()) {
                String filename = (String) iter.next();
                if (filename.length() <= adminFile.getNameBase().length())
                    continue;
                if (filename.substring(0, adminFile.getNameBase().length()).equals((String) adminFile.getNameBase())) {
                    data = SftpBaseUtil.downloadBybyte(server.HTTP_ROOT, filename, filename, sftp);
                    ServerImg img = new ServerImg();
                    img.setData(data);
                    img.setUrl(filename);
                    imgs.add(img);
                }
            }
            SftpBaseUtil.disconnect(sftp);
        } catch (Exception e) {
            imgs = new ArrayList<>();
            throw new Exception(errorInfo + e.getMessage());
        }

//        初始化默认图片
        initDefaultImg(imgs);
    }

    public List<ServerImg> getImgs() {
        return this.imgs;
    }

    /**
     * 上传图片
     *
     * @param listener 响应器
     * @param imgPath  上传路径
     */
    //    wait to be update
    public String uploadPic(String imgPath) throws Exception {
//        sftp

        String errorInfo = "上传图片异常: ";

//        获取数据
        List<String> imgName = new ArrayList<>(); //get img1.jpg..  img2.jpg xxx
        Iterator iter = imgs.iterator();
        String suffix = new String();
        while (iter.hasNext()) {
            ServerImg img = (ServerImg) iter.next();
            String name = img.getUrl();
            imgName.add(name);
        }

//        后缀
        int index = imgPath.indexOf('.');
        suffix = imgPath.substring(index, imgPath.length());

//        获取命名(防止重复命名)
        int id = GetId(imgName); //[img1.jpg, img2.jpg , xxx]

        String uploadFilename = adminFile.getNameBase() + id + suffix;
        ServerImg img = new ServerImg();
        try {
            img.setUrl(uploadFilename);//to be finished
            byte[] data = FileBaseUtil.getBytesByFile(imgPath);
            img.setData(data);

//        fstp操作
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            SftpBaseUtil.upload(adminFile.getSERVER().getHTTP_ROOT(), imgPath, uploadFilename, sftp);
            SftpBaseUtil.disconnect(sftp);
        } catch (Exception e) {
            throw new Exception(errorInfo + e.getMessage());
        }
//        数据更新
        imgs.add(img);

//        返回文件名
        return uploadFilename;
    }


    /**
     * 删除图片
     *
     * @param imgname 图片名
     */
    public void delPic(String imgname) throws Exception {

        String errorInfo = "删除图片异常: ";

//        获取数据

        Map<String, String> nimap = adminFile.getNimap();
        Map<String, String> changeMap = new HashMap<>();

        ServerImg originDefault = (defaultImg == null) ? null : defaultImg;
//        修改数据
        for (Map.Entry<String, String> e : nimap.entrySet()) {
            if (e.getValue().equals(imgname)) { //正在被引用
                if (defaultImg == null) {  //无默认图片
                    throw new Exception(errorInfo + "未设置默认图片");
                } else if (imgname.equals(defaultImg.getUrl())) {  //删除默认图片
                    throw new Exception(errorInfo + "正在删除被使用的默认图片");
                } else {
                    //替换
                    changeMap.put(e.getKey(), imgname);
                    nimap.put(e.getKey(), defaultImg.getUrl());
                }
            }
        }

        adminFile.setNimap(nimap);
        if (defaultImg != null) {
            if (imgname.equals(defaultImg.getUrl())) {
                defaultImg = null;
                adminFile.setDefaultImgurl("");
            }
        }

//        同步数据
        try {
//        update server
            check_server_info();
            ChannelSftp sftp = SftpBaseUtil.getConnectIP();
            updateServerFile(sftp);
            SftpBaseUtil.delete(server.HTTP_ROOT, imgname, sftp);
            SftpBaseUtil.disconnect(sftp);
//        update local
            updateLocalFile();
        } catch (Exception e) {
            for (Map.Entry<String, String> ch : changeMap.entrySet()) {
                changeMap.put(ch.getKey(), ch.getValue());
            }
            adminFile.setDefaultImgurl(originDefault.getUrl());
            adminFile.setNimap(nimap);
            defaultImg = originDefault;
            throw new Exception(errorInfo + e.getMessage());
        }

//        更改缓存

        Iterator imgiter = imgs.iterator();
        while (imgiter.hasNext()) {
            ServerImg img = (ServerImg) imgiter.next();
            if (img.getUrl().equals(imgname))
                imgiter.remove();
        }

    }

    //    减少使用并作为缓存记录
    public List<ShowImg> getList() throws Exception {
        List<ShowImg> simgs = new ArrayList<>();
        for (Map.Entry<String, String> e : adminFile.getNimap().entrySet()) {
            String userName = e.getKey();
            String imgName = e.getValue();
            for (ServerImg img : imgs) {
                if (img.getUrl().equals(imgName)) {
                    byte[] data = img.getData();
                    ShowImg simg = new ShowImg();
                    try {
                        simg.setImg(ImgBaseUtil.GetImageByByte(data));
                        simg.setName(userName);
                        simgs.add(simg);
                    } catch (Exception ex) {
                        throw new Exception("获取图片列表异常");
                    }
                }
            }
        }
        return simgs;
    }


    public void setDefaultImg(String imgName) throws Exception {
        String errorInfo = "设置默认图片异常: ";
        ServerImg originImg = (defaultImg == null) ? null : defaultImg;
        for (ServerImg simg : imgs) {
            if (simg.getUrl().equals(imgName))
                defaultImg = simg;
        }
        this.adminFile.setDefaultImgurl(defaultImg.getUrl());
        try {
            updateLocalFile();
        } catch (Exception e) {
            defaultImg = originImg;
            this.adminFile.setDefaultImgurl(originImg.getUrl());
            throw new Exception(errorInfo + e.getMessage());
        }
    }

    public boolean has_defaultImg() {
        return defaultImg != null;
    }

    public void setData(Data data) throws Exception {
        try {
            this.setServer(data.server_host, data.server_port, data.server_username, data.server_password, data.server_http_root);
            this.setUser_root(data.user_root);
            this.setName_Base(data.name_base);
        } catch (Exception e) {
            throw e;
        }
    }

    //    管理员文件(configRootPath\wallpaper\settings.json)init
    private String initFile(String projectPath) throws Exception {

        String errorInfo = "文件初始化失败 ";

        String wallpaperPath = projectPath + "\\wallpaper";
        File wallpaperDir = new File(wallpaperPath);
        if (!wallpaperDir.exists())
            wallpaperDir.mkdir();
        String settingPath = wallpaperPath + "\\settings.json";
        File settingFile = new File(settingPath);
//        若文件不存在, 创建文件,
        if (!settingFile.exists()) {
            try {
                settingFile.createNewFile();
                adminFile.setUSER_ROOT("");
                List<User> users = new ArrayList<>();
                adminFile.setUSERS(users);
                server = new Server();
                server.setHOST("");
                server.setPASSWORD("");
                server.setPORT("");
                server.setPASSWORD("");
                server.setHTTP_ROOT(""); //后单独处理该数据
                adminFile.setSERVER(server);
                Map<String, String> nimap = new HashMap<>();
                adminFile.setNimap(nimap);
                adminFile.setNameBase("");
                adminFile.setDefaultImgurl("");
                FileBaseUtil.writeFile(settingPath, JsonBaseUtil.ObjtoSting(adminFile));
                //若不存在,则创建setting文件并写入默认AdminFile信息
            } catch (Exception e) {
                throw new Exception(errorInfo + e.getMessage());
//                异常放外面处理
            }
        } else {
//            若存在,则读取数据至adminFile
            String adminFileText = "";
            try {
                adminFileText = FileBaseUtil.readFile(settingPath);
            } catch (Exception e) {
                throw new Exception(errorInfo + e.getMessage());
            }

//            修改缓存
            JSONObject adminFileObj = JSONObject.fromObject(adminFileText);
//            System.out.println("adminFileObj is :" + adminFileObj.toString());
            this.adminFile = (AdminFile) JSONObject.toBean(adminFileObj, AdminFile.class);

            List<User> userList = new ArrayList<>();
            JSONArray arr = adminFileObj.getJSONArray("USERS");
            for (Object obj : arr) {
                User user = new User();
                user.setName(((JSONObject) obj).getString("name"));
                user.setUrl(((JSONObject) obj).getString("url"));
                userList.add(user);
            }
//            System.out.println("ARR" + JSONArray.fromObject(userList));
            this.adminFile.setUSERS(userList);

            JSONObject jsonmap = adminFileObj.getJSONObject("nimap");
            Map<String, String> nimap = new HashMap<>();
            for (Object jkey : jsonmap.keySet()) {
                String key = jkey.toString();
                String value = jsonmap.getString(key);
                nimap.put(key, value);
            }
//            System.out.println("MAP" + JSONObject.fromObject(nimap));
            adminFile.setNimap(nimap);

            this.server = adminFile.getSERVER();
            SftpBaseUtil.setServerData(server);
//            文件损坏另外处理!!
        }
        return settingPath;
    }

    //可改为抛异常
    private void checkFile(String projectPath) throws Exception {
        File file = new File(projectPath);
        if (!file.exists())
            throw new Exception("路径错误");
    }

    private void initDefaultImg(List<ServerImg> imgs) {
        if (this.adminFile.getDefaultImgurl().equals((String) "") || this.adminFile.getDefaultImgurl() == null) {
            defaultImg = null;
            return;
        }
        for (ServerImg img : imgs) {
            if (img.getUrl().equals((String) adminFile.getDefaultImgurl()))
                defaultImg = img;
        }
    }

    //    设置服务器相关
    private void setServer(Server server) throws Exception {
        String errorInfo = "设置服务器信息异常: ";

        Server originServer = this.server;
        this.server = server;
        this.adminFile.setSERVER(server);
        SftpBaseUtil.setServerData(server);
        try {
            updateLocalFile();
        } catch (Exception e) {
//            throw
            this.server = originServer;
            this.adminFile.setSERVER(originServer);
            SftpBaseUtil.setServerData(originServer);
            throw new Exception(errorInfo + e.getMessage());
        }
    }

    private void setServer(String host, String port, String username, String password, String http_root) throws Exception {
        Server server = new Server();
        server.HOST = host;
        server.PORT = port;
        server.USERNAME = username;
        server.PASSWORD = password;
        server.HTTP_ROOT = http_root;
        try {
            this.setServer(server);
        } catch (Exception e) {
            throw e;
        }
    }

    public Server getServer() {
        return server;
    }

    private void check_server_info() throws Exception {

        if (server.HTTP_ROOT.equals((String) "")
                || server.PORT.equals((String) "")
                || server.USERNAME.equals((String) "")
                || server.PASSWORD.equals((String) "")
                || server.HTTP_ROOT.equals((String) ""))
            throw new Exception("服务器信息缺失");
        return;
    }

    private void setUser_root(String user_root) throws Exception {
        String originUSER_ROOT = this.adminFile.getUSER_ROOT();
        this.adminFile.setUSER_ROOT(user_root);
        try {
            updateLocalFile();
        } catch (Exception e) {
            this.adminFile.setUSER_ROOT(originUSER_ROOT);
            throw e;
        }
    }

    public String getUser_root() {
        return this.adminFile.getUSER_ROOT();
    }

    private void setName_Base(String name_base) throws Exception {
        String originNameBase = this.adminFile.getNameBase();
        this.adminFile.setNameBase(name_base);
        try {
            updateLocalFile();
        } catch (Exception e) {
            this.adminFile.setNameBase(originNameBase);
            throw e;
        }
    }

    public String getName_Base() {
        return this.adminFile.getNameBase();
    }

    private void updateServerFile(ChannelSftp sftp) throws Exception {
//        get file info
        String errorInfo = "更新服务器文件异常: ";

//        获取数据
        Map<String, String> nimap = adminFile.getNimap();
        List<User> users = adminFile.getUSERS();

        Map<String, String> originmap = new HashMap<>();
        for(Map.Entry<String, String> e : nimap.entrySet()){
            originmap.put(e.getKey(), e.getValue());
        }
//        修改数据
        Map<String, String> uimap = new HashMap<>();
        for (User user : users) {
            String img = nimap.get(user.getName());
            uimap.put(user.getUrl(), img);
        }
        serverFile.setUimap(uimap);

//        同步数据
        String tempFileName = "";
        try {

//        本地获得文件
            tempFileName = configRootPath + "\\wallpaper\\tempsettingFile.json";

            File tmpFile = new File(tempFileName);
            if (!tmpFile.exists())
                FileBaseUtil.createFile(configRootPath + "\\wallpaper\\tempsettingFile.json");

//        System.out.println("tempFile is : " + tempFileName);
            FileBaseUtil.writeFile(tempFileName, JsonBaseUtil.ObjtoSting(serverFile));
//            sftp操作

            List<String> fileList = SftpBaseUtil.check(adminFile.getSERVER().getHTTP_ROOT(), sftp);
            if (fileList.contains((String) "settings.json")) {
                SftpBaseUtil.delete(server.HTTP_ROOT, "settings.json", sftp);
            }
            SftpBaseUtil.upload(server.HTTP_ROOT, tempFileName, "settings.json", sftp);
        } catch (Exception e) {
            serverFile.setUimap(originmap);
            throw new Exception(errorInfo + e.getMessage());
        }
//        删除本地文件
        try {
            FileBaseUtil.deleteFile(tempFileName);
        }catch (Exception e){
            ;
        }
    }

    private void updateLocalFile() throws Exception {
//        localFile
        String errorInfo = "更新本地文件异常 ";
        try {
            FileBaseUtil.writeFile(configFilePath, JsonBaseUtil.ObjtoSting(adminFile));
        } catch (Exception e) {
            throw new Exception(errorInfo + e.getMessage());
        }
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
