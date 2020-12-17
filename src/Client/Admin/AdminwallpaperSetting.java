package Client.Admin;

import Client.DownPic.listener.RequestFinishListener;
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
    AdminFile adminFile = null; //仅在修改server时可以对该储存的server进行操作, 否则统一使用该类内部的server
    String configRootPath = null;
    String configFilePath = null;
    List<ServerImg> imgs = new ArrayList<>(); //切换为String
    ServerImg defaultImg = null;
    ServerFile serverFile = null;

    //配置文件获取这两个信息

    /**
     * 设置projectRoot and settingFileUrl
     * @param admin
     * @param listener
     * @throws Exception
     */
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

//      configFilePath include
        configFilePath = initFile(configRootPath);
//            JsonBaseUtil.ObjtoSting(adminFile);

//        log init 暂时不管
        LogBaseUtil.init(configRootPath);
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

        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
//        同步数据
        updateLocalFile();
        return true;
    }


//    用户相关

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

    public List<User> getUsers(){
        return adminFile.getUSERS();
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


    public Data getData(){
        return new Data(server.HOST,server.PORT,server.USERNAME,server.PASSWORD,server.HTTP_ROOT,adminFile.getUSER_ROOT(),adminFile.getNameBase());
    }

    public void initServerFile() throws Exception{
        serverFile = new ServerFile();
        Map<String, String> uimap = new HashMap<>();
        serverFile.setUimap(uimap);
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.disconnect(sftp);
    }


    public Server getServer() {
        return server;
    }

    public boolean server_correct(){
//        connect throw connectEroor
        if(server.HTTP_ROOT.equals((String)"")
        || server.PORT.equals((String)"")
        || server.USERNAME.equals((String)"")
        || server.PASSWORD.equals((String)"")
        || server.HTTP_ROOT.equals((String)""))
            return false;
        return true;
    }


    private void setUser_root(String user_root) {
        this.adminFile.setUSER_ROOT(user_root);
        updateLocalFile();
    }

    public String getUser_root() {
        return this.adminFile.getUSER_ROOT();
    }

    //设置命名规范相关(可去除)
    private void setName_Base(String name_base) {
        this.adminFile.setNameBase(name_base);
        updateLocalFile();
    }

    public String getName_Base() {
        return this.adminFile.getNameBase();
    }

    //    获取图片列表
    public void initImgs() {
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
                try {
                    data = SftpBaseUtil.downloadBybyte(server.HTTP_ROOT, filename, filename, sftp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ServerImg img = new ServerImg();
                img.setData(data);
                img.setUrl(filename);
                imgs.add(img);
            }
        }
        SftpBaseUtil.disconnect(sftp);
//        System.out.println("List is : " + JsonBaseUtil.ObjtoSting(imgs));
    }

    public List<ServerImg> getImgs(){
        return this.imgs;
    }

    /**
     * 上传图片
     *
     * @param listener 响应器
     * @param imgPath  上传路径
     */


    //    wait to be update
    public String uploadPic(String imgPath) {
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
//        System.out.println(id);
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
        return uploadFilename;
    }



    /**
     * 删除图片 待完成-> 默认图片的相关处理
     *
     * @param id
     */
    public void delPic(String imgname) {

//        check map
        String name = imgname; //to be finished

        String defaultImgName = defaultImg.getUrl(); //to be finished(throw)

        Iterator imgiter = imgs.iterator();
        while(imgiter.hasNext()){
            ServerImg img = (ServerImg) imgiter.next();
            if(img.getUrl().equals(name))
                imgiter.remove();
        }

//        change map with default img
        Map<String, String> nimap = adminFile.getNimap();
        for(Map.Entry<String, String> e : nimap.entrySet()){
            if(e.getValue().equals(name)){
                if(defaultImgName != null)
                    e.setValue(defaultImgName);
            }
        }

        adminFile.setNimap(nimap);

//        update server
        ChannelSftp sftp = SftpBaseUtil.getConnectIP();
        updateServerFile(sftp);
        SftpBaseUtil.delete(server.HTTP_ROOT, imgname, sftp);
        SftpBaseUtil.disconnect(sftp);

//        update local
        updateLocalFile();
    }

    //    减少使用并作为缓存记录
    public List<ShowImg> getList(){
        List<ShowImg> simgs = new ArrayList<>();
        for(Map.Entry<String, String> e : adminFile.getNimap().entrySet()){
            String userName = e.getKey();
            String imgName = e.getValue();
            for(ServerImg img : imgs){
                if(img.getUrl().equals(imgName)){
                    byte[] data = img.getData();
                    ShowImg simg = new ShowImg();
                    try{
                        simg.setImg(ImgBaseUtil.GetImageByByte(data));
                        simg.setName(userName);
                        simgs.add(simg);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
        return simgs;
    }

    public void setDefaultImg(String imgName) {
        for(ServerImg simg : imgs){
            if(simg.getUrl().equals(imgName))
                defaultImg = simg;
        }
        this.adminFile.setDefaultImgurl(defaultImg.getUrl());
        updateLocalFile();
    }

    public void setData(Data data){
        this.setServer(data.server_host, data.server_port, data.server_username, data.server_password, data.server_http_root);
        this.setUser_root(data.user_root);
        this.setName_Base(data.name_base);
    }

    //    管理员文件(configRootPath\wallpaper\settings.json)init
    private String initFile(String projectPath) {
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
                adminFile = new AdminFile();
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
                e.printStackTrace();
//                异常放外面处理
            }
        } else {
//            若存在,则读取数据至adminFile
            String adminFileText = FileBaseUtil.readFile(settingPath);
            JSONObject adminFileObj = JSONObject.fromObject(adminFileText);
            System.out.println("adminFileObj is :" +  adminFileObj.toString());
            this.adminFile = (AdminFile) JSONObject.toBean(adminFileObj, AdminFile.class);

            List<User> userList = new ArrayList<>();
            JSONArray arr = adminFileObj.getJSONArray("USERS");
            for(Object obj : arr){
                User user = new User();
                user.setName(((JSONObject)obj).getString("name"));
                user.setUrl(((JSONObject)obj).getString("url"));
                userList.add(user);
            }
            System.out.println("ARR" + JSONArray.fromObject(userList));
            this.adminFile.setUSERS(userList);

            JSONObject jsonmap = adminFileObj.getJSONObject("nimap");
            Map<String, String> nimap = new HashMap<>();
            for(Object jkey : jsonmap.keySet()){
                String key = jkey.toString();
                String value = jsonmap.getString(key);
                nimap.put(key, value);
            }
            System.out.println("MAP" + JSONObject.fromObject(nimap));
            adminFile.setNimap(nimap);

            this.server = adminFile.getSERVER();
            SftpBaseUtil.setServerData(server);
//            文件损坏另外处理!!
        }
        return settingPath;
    }

//可改为抛异常
    private boolean checkFile(String projectPath) {
        File file = new File(projectPath);
        if (!file.exists())
            return false;
        return true;
    }


    //    设置服务器相关
    private void setServer(Server server) {
        this.server = server;
        this.adminFile.setSERVER(server);
        SftpBaseUtil.setServerData(server);
        updateLocalFile();
    }

    private void setServer(String host, String port, String username, String password, String http_root){
        Server server = new Server();
        server.HOST = host;
        server.PORT = port;
        server.USERNAME = username;
        server.PASSWORD = password;
        server.HTTP_ROOT = http_root;
        this.setServer(server);
    }


    //imgList and defaultImg 抛出异常
    private void updateServerFile(ChannelSftp sftp) {
//        get file info
        if(server.HTTP_ROOT.equals("") ||
            server.HOST.equals("") ||
            server.PORT.equals("") ||
            server.USERNAME.equals("") ||
            server.PASSWORD.equals("")){
            return ;
//            抛异常
        }

        Map<String, String> nimap = adminFile.getNimap();
        List<User> users = adminFile.getUSERS();
        Map<String, String> uimap = new HashMap<>();
        for(User user : users){
            String img = nimap.get(user.getName());
            uimap.put(user.getUrl(), img);
        }
        serverFile.setUimap(uimap);

//        serverFile
//        create new file and write

        String tempFileName = configRootPath + "\\wallpaper\\tempsettingFile.json";
        if(!checkFile(tempFileName)){
            FileBaseUtil.createFile(configRootPath + "\\wallpaper\\tempsettingFile.json");
        }

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
