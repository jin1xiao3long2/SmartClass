package Client.DownPic.test;

import Client.DownPic.utils.BaseUtils.FileBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import Client.DownPic.wallpaperSetting;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

    @Test
    public void test3(){
        try{
            ServerImg img = new ServerImg();
            img.setId(1);
//            img.setUrl("abc");
            ServerImg img2 = new ServerImg();
            img2.setId(2);
//            img2.setUrl("def");
            ServerFile file = new ServerFile();
//            file.addImg(img);
//            file.addImg(img2);
            String str1 = JsonBaseUtil.ObjtoSting(img);
            String str2 = JsonBaseUtil.ObjtoSting(file);
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
        server.setHost("192.168.26.128"); //改
        server.setPort("22");
        server.setUsername("jol");
        server.setPassword("jxda7797797");
        server.setUrl("/home/jola/wallpaper");
        AdminFile mainUser = new AdminFile();
//        mainUser.setFilepath("D:\\JAVAProjects\\SmartClass");
//        wallpaperSetting MainSetting = new wallpaperSetting();
//        MainSetting.init(server, mainUser);
//        LogBaseUtil.init(mainUser.getFilepath());
//        MainSetting.uploadPic(mainUser.getFilepath()+"\\wallpaper\\imgs\\1.jpg");
    }

    @Test
    public void test5(){
        FileBaseUtil.writeFile("D:\\JAVAProjects\\SmartClass\\123.txt", "123");
        FileBaseUtil.appendFile("D:\\JAVAProjects\\SmartClass\\123.txt", "123");
        FileBaseUtil.appendlnFile("D:\\JAVAProjects\\SmartClass\\123.txt", "123");
        FileBaseUtil.appendFile("D:\\JAVAProjects\\SmartClass\\123.txt", "123");
    }

    class Foo{
        public String key;
        public String name;

        public Foo(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public Foo() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
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
        Server server = new Server("host","port","username","password","filepath");
        String URL_ROOT = "URL_ROOT";
        AdminFile adminFile = new AdminFile();
        adminFile = new AdminFile();
        adminFile.setSERVER(server);
        List<User> users = new ArrayList<>();
        adminFile.setUSERS(users);
        Map<User, ServerImg> map = new HashMap<>();
        adminFile.setMaps(map);
        adminFile.setURL_ROOT(URL_ROOT);
        JSONObject obj = JSONObject.fromObject(adminFile);
        System.out.println(obj);

        User user = new User();
        user.setUrl("URL");
        user.setName("name");
        obj.getJSONArray("USERS").add(JSONObject.fromObject(user));
        System.out.println(obj);

        AdminFile newfile = (AdminFile) JSONObject.toBean(obj, AdminFile.class);
        System.out.println(newfile.getURL_ROOT());
    }

}
