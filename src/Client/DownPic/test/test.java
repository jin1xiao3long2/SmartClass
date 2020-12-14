package Client.DownPic.test;

import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.FileBaseUtil;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.*;
import entity.ServerImg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class test {

    @Test
    public void test3(){
        try{
            ServerImg img = new ServerImg();
//            img.setId(1);
//            img.setUrl("abc");
            ServerImg img2 = new ServerImg();
//            img2.setId(2);
//            img2.setUrl("def");
            ServerFile file = new ServerFile();
//            file.addImg(img);
//            file.addImg(img2);
            java.lang.String str1 = JsonBaseUtil.ObjtoSting(img);
            java.lang.String str2 = JsonBaseUtil.ObjtoSting(file);
            System.out.println(str1);
            System.out.println(str2);
            JSONObject obj1 = JSONObject.fromObject(img);
            JSONObject obj2 = JSONObject.fromObject(str1);
            System.out.println(obj1);
            System.out.println(obj2);
            int id = obj1.getInt("id");
            System.out.println(id);
            JSONObject obj3 = JSONObject.fromObject(file);
            JSONArray obj4 = obj3.getJSONArray("imgs");
            System.out.println(obj4.getJSONObject(1).toString());
            System.out.println(obj4.getJSONObject(1).getInt("id"));
            obj4.add(obj1);
            System.out.println(obj3);
            //            JSONObject obj2 = obj1.getJSONObject("imgs");
//            System.out.println(obj2.toString());
            JSONArray jsonArray = null;
            if(obj1 != null){
                obj1.toJSONArray(jsonArray);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test4(){
        entity.Server server = new Server();
        server.setHOST("192.168.26.128"); //改
        server.setPORT("22");
        server.setUSERNAME("jol");
        server.setPASSWORD("jxda7797797");
        server.setHTTP_ROOT("/home/jola/wallpaper");
        AdminFile mainUser = new AdminFile();
//        mainUser.setFilepath("D:\\JAVAProjects\\SmartClass");
//        wallpaperSetting MainSetting = new wallpaperSetting();
//        MainSetting.init(server, mainUser);
//        LogBaseUtil.init(mainUser.getFilepath());
//        MainSetting.uploadPic(mainUser.getFilepath()+"\\wallpaper\\imgs\\1.jpg");
    }

    @Test
    public void test5(){
        File file = new File("D:\\JAVAProjects\\settings.json");
        System.out.println(file.getName());
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
    public void test6(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "value");
        jsonObject.put("key", "123");
        Foo foo =(Foo) JSONObject.toBean(jsonObject, Foo.class);
        System.out.println(foo.name + foo.key);
        JSONArray array = new JSONArray();
        array.add(jsonObject);
        jsonObject.put("name", "vvallue");
        jsonObject.put("key", "2233");
        array.add(jsonObject);
        JSONObject mainObj = new JSONObject();
        mainObj.put("arr", array);

//        System.out.println(array);
//        System.out.println(mainObj);
        jsonObject.put("name", "hh");
        jsonObject.put("key","333");
        JsonBaseUtil.addDataInArr(mainObj, "arr", jsonObject);
        System.out.println(mainObj);
//        List<String> list = (List)JSONArray.toCollection(mainObj.getJSONArray("arr"), Foo.class);
        JSONArray arr = mainObj.getJSONArray("arr");
        int length = arr.size();
        for(int i = 0; i < length; i++){
            JSONObject obj = JSONObject.fromObject(arr.getString(i));
            System.out.println(obj);
        }
//        System.out.println("List is : " + list);
        JsonBaseUtil.delDataInArr(mainObj, "arr", "name", (Object)"value");
        System.out.println(mainObj);

    }

    @Test
    public void test7(){
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
