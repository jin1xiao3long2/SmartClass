package zrkc.group.new_ui.component;

import zrkc.group.core.AdminwallpaperSetting;
import zrkc.group.javabean.*;
import zrkc.group.listener.RequestFinishListener;
import zrkc.group.new_ui.config.AppParameters;


import javax.swing.*;
import java.util.List;

public class MainFrame extends JFrame {

    private MainPanel panel = null;
    private AdminwallpaperSetting mainApp = null;
    private Admin admin = null;
    private List<ShowImg> showImgs = null;


    public void setPanel(MainPanel panel) {
        this.panel = panel;
    }

    public MainFrame(AdminwallpaperSetting mainapp, Admin admin) {
        this.mainApp = mainapp;
        this.admin = admin;
        Init();
    }

    private void Init() {
        //        设置名字
        this.setTitle("四川大学壁纸更换系统");
//        设置关闭属性
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        设置窗口大小
        AppParameters.init_Screen_Size();//初始化,待改动
        int width = AppParameters.Screen_Width;
        int height = AppParameters.Screen_Height;
        this.setSize(width / 2, height / 2);
//        不允许改变大小
        this.setResizable(false);
//        居中
        this.setLocation(width / 4, height / 4);
//        导入主要内容
        this.panel = new MainPanel(this);
        panel.setBounds(0, 0, width, height);

//        后端内容初始化
//        try {
//            mainApp.init(admin);
//        } catch (Exception e) {
//            e.printStackTrace();
//            //需要报错
//            System.exit(-1);
//        }

//        更新imgs
//        updateData();

        add(this.panel);
        this.setUndecorated(false);
    }

    public MainPanel getPanel() {
        return this.panel;
    }

    private void updateData() {
        try {
            mainApp.initImgs();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                showImgs = mainApp.getList();
            } catch (Exception e) {
                ;
            }
        }
    }

    public void delUser(String username, RequestFinishListener listener) {
        List<User> users = mainApp.getUsers();
        User deluser = new User();
        for (User user : users) {
            if (user.getName().equals((String) username)) {
                deluser = user;
            }
        }
        try {
            mainApp.delUser(deluser);
            showImgs = mainApp.getList();
            listener.log("删除用户成功");
        } catch (Exception e) {
            listener.error(e);
        }
    }

    public void changePaper(String username, ServerImg img, RequestFinishListener listener) {
        List<User> users = mainApp.getUsers();
        User chuser = new User();
        for (User user : users) {
            if (user.getName().equals((String) username)) {
                chuser = user;
            }
        }
        try {
            mainApp.changeWallpaper(chuser, img);
            showImgs = mainApp.getList();
            listener.log("更改壁纸成功");
        } catch (Exception e) {
            listener.error(e);
        }
    }

    public String uploadPic(String path, RequestFinishListener listener) {
        try {
            String name = mainApp.uploadPic(path);
            listener.log("上传图片成功");
            return name;
        } catch (Exception e) {
            listener.error(e);
            return "";
        }
    }

    public AdminwallpaperSetting getMainApp() {
        return mainApp;
    }

    public void setMainApp(AdminwallpaperSetting mainApp) {
        this.mainApp = mainApp;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<ShowImg> getShowImgs() {
        return showImgs;
    }

    public void setShowImgs(List<ShowImg> showImgs) {
        this.showImgs = showImgs;
    }

    public void delPic(String imgName, RequestFinishListener listener) {
        try {
            mainApp.delPic(imgName);
            listener.log("删除图片成功");
        } catch (Exception e) {
            listener.error(e);
        }
    }

    public void setDefaultPic(String imgName, RequestFinishListener listener) {
        try {
            mainApp.setDefaultImg(imgName);
            listener.log("上传图片成功");
        } catch (Exception e) {
            listener.error(e);
        }
    }

    public boolean has_defaultImg() {
        return mainApp.has_defaultImg();
    }

    public List<ServerImg> getImgs() {
        return mainApp.getImgs();
    }

    public void setDefaultImg(String url, RequestFinishListener listener) {
        try {
            mainApp.setDefaultImg(url);
            listener.log("设置默认图片成功");
        } catch (Exception e) {
            listener.error(e);
        }
    }

    public void setData(Data data, RequestFinishListener listener) {

        try {
            mainApp.setData(data);
            listener.log("修改成功");
        } catch (Exception e) {
            listener.error(e);
        } finally {
            updateData();
        }
    }

}
