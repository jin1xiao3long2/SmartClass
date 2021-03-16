package zrkc.group.new_ui.component;

import zrkc.group.javabean.ShowImg;
import zrkc.group.javabean.User;
import zrkc.group.new_ui.config.AppParameters;
import zrkc.group.utils.BaseUtils.FileBaseUtil;
import zrkc.group.utils.BaseUtils.ImgBaseUtil;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    //    主窗口
    private MainFrame mainFrame = null;
    //    最大的画布
    private JLayeredPane layeredPane = null;
    //    标题
    private JLabel Title = null;
    //    选择校区的下拉框
    private JComboBox CampusBox = null;
    //    选择教学楼的下拉框
    private JComboBox BuildingBox = null;
    //    选择楼层的下拉框
    private JComboBox FloorBox = null;
    //    设置按钮
    private JButton SettingButton = null;
    //    教室展示用的画布..
    private JPanel Classes = null; //该项必然修改
    //    设置界面
    private JFrame Settings = null;
    //    增加用户界面
    private JFrame AddUserFrame = null;


    //    修改用户壁纸界面
    private ClassInfoFrame ChangeUserFrame = null;
    //    选择图片界面
    private JFrame UploadPicFrame = null;

    public MainPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setLayout(null);
        initUI();
        System.out.println("2");
    }

    private void initApp() {

    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private void initUI() {
//        初始化标题
        Title = new JLabel();
        Title.setText("四川大学壁纸更换系统");
//        Title.setLocation(100, 50);
        Title.setBounds(0, 0, 200, 20);//width是size * 字数, 高度为size则与顶端对齐
        Font font = new Font("黑体", Font.PLAIN, 20);
        Title.setFont(font);
        System.out.println(Title.getSize() + "" + Title.getLocation());
//        初始化配置工具
        AppParameters para = new AppParameters();

//        新建更换壁纸窗口
        ChangeUserFrame = new ClassInfoFrame(null, this);
//        初始化某Lable
        JLabel label = new JLabel();
        Image img = null;
        try {
            img = ImgBaseUtil.GetImageByByte(FileBaseUtil.getBytesByFile("D:\\JAVAprojects\\SmartClass\\wallpaper\\abc.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShowImg i = new ShowImg();
        i.setImg(img);
        i.setName("ImgName");
        User user = new User();
        user.setName("综合楼A304");
        user.setUrl("url");
        ClassImg ii = new ClassImg(50, 50, AppParameters.ShowImg_Width, AppParameters.ShowImg_Height,
                AppParameters.ImgScreen_Width_Interval, AppParameters.ImgScreen_Width_Interval, i, user, this);


//        存放组件
        this.add(Title);
//        this.add(label);
        this.add(ii);
//        int add = 0;
//        layeredPane.add(Title, add++);
//        System.out.println("3 " + Title.getText());
//        layeredPane.add(label, add++);
//        导入组件
//        this.add(layeredPane);
    }

    public ClassInfoFrame getChangeUserFrame() {
        return ChangeUserFrame;
    }
}
