package Client.ui;

import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        if(login());
        Admin admin = new Admin();
        String projectPath = "D:\\JAVAProjects\\SmartClass";
        AdminFile adminFile = new AdminFile();
        admin.setProjectPath(projectPath);
        admin.setAdminFile(adminFile);
        AdminwallpaperSetting mainApp = new AdminwallpaperSetting();

        try{
            mainApp.init(admin, new RequestFinishListener() {
                @Override
                public void log(String response) {
                    System.out.println(response);
                }

                @Override
                public void error(Exception ex) {
                    ex.printStackTrace();
                }
            });
        }catch (Exception e){
//
            e.printStackTrace();
        }

//        修改server
        Server server = setServer("192.168.26.1","22", "jola", "jxda7797797", "/home/jola/projectPath");
        mainApp.setServer(server);
        System.out.println(JsonBaseUtil.ObjtoSting(mainApp.getServer()));
//        初始化
        mainApp.initServer();

        //
        mainApp.setName_Base("img");
        mainApp.getImgs();

        ServerImg deImg = new ServerImg();
        deImg.setUrl("img2.jpg");
        mainApp.setDefaultImg(deImg);

        User new_user = new User();
        new_user.setName("哈哈哈");
        new_user.setUrl("abc");
        ServerImg user_img = new ServerImg();
        user_img.setUrl("img3.jpg");
//        mainApp.addUser(new_user, "img3.jpg");
        System.out.println(JsonBaseUtil.ObjtoSting(mainApp.getMap()));
//        mainApp.delPic(user_img);
        User new_user1 = new User();
        new_user.setName("嘻嘻嘻");
        new_user.setUrl("ace");
//        ServerImg user_img = new ServerImg();
        mainApp.addUser(new_user1, "img3.jpg");
    }

    private static boolean login(){
        return true;
    }

    private static void checkPics(){
    }

    private static Server setServer(String host, String port, String username, String password, String http_root){
        Server server = new Server();
        server.setHOST(host);
        server.setPORT(port);
        server.setUSERNAME(username);
        server.setPASSWORD(password);
        server.setHTTP_ROOT(http_root);
        return server;
    }
}