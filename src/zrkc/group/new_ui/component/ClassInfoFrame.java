package zrkc.group.new_ui.component;

import zrkc.group.javabean.ShowImg;
import zrkc.group.javabean.User;
import zrkc.group.new_ui.config.AppParameters;

import javax.swing.*;



public class ClassInfoFrame extends JFrame {

    private JLabel originImg = null;
    private JLabel newImg = null;
    private JLabel ClassInfo = null;
    private JButton Confirm = null;
    private JButton Cancel = null;
    private ShowImg img = null;
    private User user = null;
    private JPanel panel = null;


    public JLabel getOriginImg() {
        return originImg;
    }

    public void setOriginImg(JLabel originImg) {
        this.originImg = originImg;
    }

    public ClassInfoFrame(ShowImg img, MainPanel panel){
        this.img = img;
        this.panel = panel;
        setFramePara();
    }

    private void setFramePara(){
        //        设置名字
        this.setTitle("更换壁纸");
//        设置关闭属性
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        设置窗口大小
//        AppParameters.init_Screen_Size();//初始化,待改动
        int width = AppParameters.Screen_Width;
        int height = AppParameters.Screen_Height;
        this.setSize(width/2, height/2);
//        不允许改变大小
        this.setResizable(false);
//        居中
        this.setLocation(width/4, height/4);
//        设置边框
        this.setUndecorated(false);

    }

    private void setImg(ShowImg img){

    }
}
