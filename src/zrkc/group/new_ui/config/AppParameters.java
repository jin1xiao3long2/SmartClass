package zrkc.group.new_ui.config;

import zrkc.group.core.AdminwallpaperSetting;
import zrkc.group.javabean.Admin;

import java.awt.*;

public class AppParameters {

    public static int Screen_Width = 0;
    public static int Screen_Height = 0;
    public static AdminwallpaperSetting main_App;
    public static int ShowImg_Width = 0;
    public static int ShowImg_Height = 0;
    public static int ImgScreen_Width = 0;
    public static int ImgScreen_Height = 0;
    public static int ImgScreen_Width_Interval = 0;
    public static int ImgScreen_Height_Interval = 0;


    public static void init_Screen_Size(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Screen_Height = screenSize.height;
        Screen_Width = screenSize.width;
        ImgScreen_Height = Screen_Height * 3 / 4;
        ImgScreen_Width = Screen_Width * 3 / 4;
        ShowImg_Height = ImgScreen_Height / 5;
        ShowImg_Width = ImgScreen_Width / 6;
        ImgScreen_Height_Interval = ImgScreen_Height / 55;
        ImgScreen_Width_Interval = ImgScreen_Width / 60;
    }

    public static void init_data(AdminwallpaperSetting mainApp, Admin admin){
        main_App = mainApp;
        try {
            main_App.init(admin);
        }catch (Exception e){
            e.printStackTrace();//待修改
        }
    }

    public static void update_data(){
//        更新数据
    }
}
