package Client.ui;

import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.ImgBaseUtil;
import entity.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {


        Admin admin = new Admin();
        String projectPath = "D:\\JAVAProjects\\SmartClass";
        AdminFile adminFile = new AdminFile();
        admin.setProjectPath(projectPath);
        admin.setAdminFile(adminFile);
        AdminwallpaperSetting mainApp = new AdminwallpaperSetting();
        try {
            mainApp.init(admin, new RequestFinishListener() {
                @Override
                public void log(String response) {
                    System.out.println("!23");
                }

                @Override
                public void error(Exception ex) {
                    ex.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        MainWindow window = new MainWindow();
        window.Init(mainApp);


    }



    /**
     * 转换类型
     * @param imgs <用户名 + 图片(url, byte[])>
     * @return List<ShowImg(用户名, image)>
     */
    private static List<ShowImg> conver_to_showImgs(Map<String, ServerImg> imgs) {
//
        List<ShowImg> showImgs = new ArrayList<>();
        for (Map.Entry<String, ServerImg> e : imgs.entrySet()) {
            ShowImg img = new ShowImg();
            img.setName(e.getKey());
            Image image = null;
            try {
                image = ImgBaseUtil.GetImageByByte(e.getValue().getData());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            img.setImg(image);
            showImgs.add(img);
        }
        return showImgs;
    }

    private static boolean login() {
        return true;
    }

    private static void checkPics() {
    }

    private static Server setServer(String host, String port, String username, String password, String http_root) {
        Server server = new Server();
        server.setHOST(host);
        server.setPORT(port);
        server.setUSERNAME(username);
        server.setPASSWORD(password);
        server.setHTTP_ROOT(http_root);
        return server;
    }
}