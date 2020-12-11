package Client.DownPic.test;

import Client.DownPic.utils.CommUtil;
import Client.DownPic.wallpaperSetting;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.MainUser;
import entity.Server;
import entity.SysWallpaperFile;
import entity.SysWallpaperImg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class test {
    @Test
    public void test1(){
        int[] a = {1,3,5,2,43,22,76,33,21,-1};
        System.out.println(CommUtil.getMax(a));
    }

    @Test
    public void test2(){
        try{
            CommUtil.saveLog(1, "message");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        try{
            SysWallpaperImg img = new SysWallpaperImg();
            img.setId(1);
//            img.setUrl("abc");
            SysWallpaperImg img2 = new SysWallpaperImg();
            img2.setId(2);
//            img2.setUrl("def");
            SysWallpaperFile file = new SysWallpaperFile();
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
        server.setPath("/home/jola/wallpaper");
        entity.MainUser mainUser = new MainUser();
        mainUser.setFilepath("D:\\JAVAProjects\\SmartClass");
        wallpaperSetting MainSetting = new wallpaperSetting();
        boolean result = MainSetting.init(server, mainUser);
        MainSetting.uploadPic(mainUser.getFilepath()+"\\wallpaper\\imgs\\1.jpg");
    }

    @Test
    public void test5(){
        String uploadFilename = "1.jpg";
        int index = uploadFilename.indexOf('.');
        String backwords = uploadFilename.substring(index, uploadFilename.length());
        System.out.println(backwords);

    }

    class Foo{
        public String name;
        public String key;
    }

    @Test
    public void test6(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "value");
        jsonObject.put("key", "123");
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

}
