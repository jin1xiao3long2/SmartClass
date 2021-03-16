package zrkc.group.application;

import zrkc.group.core.AdminwallpaperSetting;
import zrkc.group.javabean.Admin;
import zrkc.group.javabean.AdminFile;
import zrkc.group.new_ui.component.MainFrame;

public class AppStart {

    public static void main(String[] args){
        Admin admin = new Admin();
        String projectPath = "D:\\JAVAProjects\\SmartClass";
        AdminFile adminFile = new AdminFile();
        admin.setProjectPath(projectPath);
        admin.setAdminFile(adminFile);
        AdminwallpaperSetting mainApp = new AdminwallpaperSetting();
        MainFrame frame = new MainFrame(mainApp, admin);
        frame.setVisible(true);
    }



}
