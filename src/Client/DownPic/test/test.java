package Client.DownPic.test;

//import Client.Admin.AdminwallpaperSetting;
import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.*;
import entity.*;
import entity.ServerImg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;


import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class test {

    @Test
    public void testJSON(){
        User user = new User();
        user.setName("韩尚坤");
        user.setUrl("测试url");
        AdminFile adminFile = new AdminFile();
        try {
            String jsonStr = JsonBaseUtil.ObjtoSting(user);
            System.out.println("测试JSONBaseUtil.ObjToString(obj)  " + jsonStr);
            JSONObject obj = JSONObject.fromObject(adminFile);
            System.out.println("查看原始数据 " + obj);
            JsonBaseUtil.addDataInArr(obj, "USERS", user);
            System.out.println("查看添加后数据 " + obj);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFile(){
        String PathRoot = "D:\\JAVAprojects\\韩尚坤";
        try{
            String testFilename = PathRoot + "\\test.txt";
            FileBaseUtil.createFile(testFilename);
            FileBaseUtil.appendlnFile(testFilename, "first line word");
            FileBaseUtil.appendlnFile(testFilename, "new line word");
            String text = FileBaseUtil.readFile(testFilename);
            System.out.println(text);
            FileBaseUtil.writeFile(testFilename, "testWord");
            text = FileBaseUtil.readFile(testFilename);
            System.out.println(text);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testLogBase(){

        String PathRoot = "D:\\JAVAprojects\\韩尚坤";
        LogBaseUtil.init(PathRoot);
        LogBaseUtil.saveLog(1, "success1");
        LogBaseUtil.saveLog(1, "success2");
        LogBaseUtil.saveLog(1, "success3");
        LogBaseUtil.saveLog(0, "fail1");
        LogBaseUtil.saveLog(0, "fail2");
        LogBaseUtil.saveLog(0, "fail3");


    }

    class Foo{
        public java.lang.String key;
        public java.lang.String name;

        public Foo(java.lang.String key, java.lang.String name) {
            this.key = key;
            this.name = name;
        }

        public Foo() {
        }

        public java.lang.String getKey() {
            return key;
        }

        public void setKey(java.lang.String key) {
            this.key = key;
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }


    }

    @Test
    public void testImgBase(){
        Server server = new Server();
        server.HOST = "192.168.233.108";
        server.PORT = "22";


    }

    @Test
    public void test7(){
        Admin admin = new Admin();
        String projectPath = "D:\\JAVAProjects\\SmartClass";
        AdminFile adminFile = new AdminFile();
        admin.setProjectPath(projectPath);
        admin.setAdminFile(adminFile);
//        AdminwallpaperSetting mainApp = new AdminwallpaperSetting();

        try{
//            mainApp.init(admin, new RequestFinishListener() {
//                @Override
//                public void log(String response) {
//                    System.out.println(response);
//                }
//
//                @Override
//                public void error(Exception ex) {
//                    ex.printStackTrace();
//                }
//            });
        }catch (Exception e){
//
            e.printStackTrace();
        }

//        修改server
        Server server = setServer("192.168.26.1","22", "jola", "jxda7797797", "/home/jola/projectPath");
//        mainApp.setServer(server);
//        System.out.println(JsonBaseUtil.ObjtoSting(mainApp.getServer()));
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

    @Test
    public void test8(){
//        byte[] data = FileBaseUtil.getBytesByFile("D:\\JAVAProjects\\SmartClass\\1.jpg");
        Image img = null;
        try{
//             img = ImgBaseUtil.GetImageByByte(data);
        }catch (Exception e){
            e.printStackTrace();
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        // 创建及设置窗口
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 添加 "Hello World" 标签
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        // 显示窗口
        frame.pack();
        frame.setVisible(true);

    }

    @Test
    public void test10(){
        List<String> ls = new ArrayList<>();
        ls.add("abc");
        System.out.println(ls);
    }

    @Test
    public void testaddUser(){
        Admin admin = new Admin();
        AdminFile adminFile = new AdminFile();
        admin.setAdminFile(adminFile);
        admin.setProjectPath("D:\\JAVAProjects\\SmartClass\\test");
        AdminwallpaperSetting mainApp = new AdminwallpaperSetting();
        try{
            mainApp.init(admin);
//            mainApp.initServerFile();
            mainApp.initImgs();
            User user = new User();
            user.setUrl("url1");
            user.setName("江安校区一教B304");
            mainApp.addUser(user, "img2.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
