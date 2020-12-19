package Client.ui;

import java.awt.*;
//输入一个list<Image,ID>，输出图片，点击时获取图片id
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;

import Client.Admin.AdminwallpaperSetting;
import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.JsonBaseUtil;
import entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MainWindow extends JFrame {
    public JFrame frame = new JFrame("Login Example");//初始界面，为了便于跳转页面
    public List<ShowImg> showImgs = new ArrayList<>();
    public AdminwallpaperSetting mainApp = new AdminwallpaperSetting();
    public JScrollPane scrollpane = null;
    public JPanel panel = new JPanel();
    private Admin admin = null;


    public void repaint() {
        super.repaint();

        frame.getContentPane().removeAll();
        panel.removeAll();
        placeComponentsHome(panel);
        JScrollPane scrollpane = new JScrollPane(panel);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollpane = scrollpane;
        this.frame.getContentPane().add(this.scrollpane);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public void Init(AdminwallpaperSetting mainapp, Admin admin) {
        frame.setPreferredSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setBounds(screenWidth / 4, screenHeight / 4, 640, 480);
        mainApp = mainapp;

        this.admin = admin;
        try {
            mainapp.init(admin);
        } catch (Exception e) {
            MainWindow.showWarning(e.getMessage());
            System.exit(-1);
        }

        updateData();
        placeComponentsHome(panel);
        panel.setPreferredSize(new Dimension(640, 480));
        JScrollPane scrollpane = new JScrollPane(panel);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollpane = scrollpane;
        frame.getContentPane().add(this.scrollpane);
//		frame.add(scrollpane);
        frame.pack();
        frame.setVisible(true);
    }

    public static class ChangePageListener implements ActionListener//点击返回键的操作
    {
        private List<ShowImg> imgs = new ArrayList<ShowImg>();
        private JFrame frame;
        private List<Image> listImgs = new ArrayList<>();

        public List<ShowImg> getImgs() {
            return imgs;
        }

        public void setImgs(List<ShowImg> imgs) {
            this.imgs = imgs;
        }


        public JFrame getFrame() {
            return frame;
        }

        public void setFrame(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.frame.dispose();
        }
    }

    public void updateData() {
        JDialog jDialog = new JDialog(frame, "正在初始化图片");
//        jDialog.setPreferredSize(new Dimension(120, 35));
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        jDialog.setBounds(screenWidth / 2 -  120, screenHeight / 2 - 15 , 180, 60);
        jDialog.setAlwaysOnTop(true);
        jDialog.setModal(false);
        jDialog.setVisible(true);

        try {
            mainApp.initImgs();
            mainApp.initServerFile();

            jDialog.setVisible(false);
            jDialog.dispose();
        } catch (Exception e) {

            jDialog.setVisible(false);
            jDialog.dispose();
            this.showWarning("初始化图片失败: " + e.getMessage());
        } finally {
            try {
                showImgs = mainApp.getList();
            } catch (Exception ex) {
                ;
            }
        }
    }


    public void placeComponentsHome(JPanel panel)//在页面中放置组件
    {//base on list get paint the panel
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(120, 16, 400, 40);
        Font font = new Font("宋体",Font.BOLD,40);
        lblNewLabel.setFont(font);
        lblNewLabel.setText("四川大学壁纸管理");
        panel.add(lblNewLabel);

        /*
        -----------------------begin------------------------
         */
        /*
        JComboBox<String> campusComboBox=new JComboBox<>();
        campusComboBox.addItem("望江校区");
        campusComboBox.addItem("江安校区");
        campusComboBox.addItem("华西校区");
        campusComboBox.setBounds(10, 70, 80, 21);
        panel.add(campusComboBox);

        JComboBox<String> BuidingComboBox=new JComboBox<>();
        BuidingComboBox.addItem("第一教学楼A座");
        BuidingComboBox.addItem("第一教学楼B座");
        BuidingComboBox.addItem("第一教学楼C座");
        BuidingComboBox.addItem("第一教学楼D座");
        BuidingComboBox.addItem("文科楼一座");
        BuidingComboBox.addItem("文科楼二座");
        BuidingComboBox.addItem("文科楼三座");
        BuidingComboBox.setBounds(100, 70, 100, 21);
        panel.add(BuidingComboBox);

        JComboBox<String> FloorComboBox=new JComboBox<>();
        FloorComboBox.addItem("f1");
        FloorComboBox.addItem("f2");
        FloorComboBox.addItem("f3");
        FloorComboBox.addItem("f4");
        FloorComboBox.addItem("f5");
        FloorComboBox.addItem("f6");
        FloorComboBox.setBounds(210, 70, 80, 21);
        panel.add(FloorComboBox);

        
         */
        /*
        -----------------------end-------------------------
         */

        JButton option = new JButton();
        option.setText("设置");
        option.setSelected(false);
        option.setBounds(347, 74, 34, 20);
        option.setMargin(new Insets(0, 0, 0, 0));
        option.addActionListener(new SwitchToOptionListener(option.getText(), this));
        panel.add(option);

        JButton flush = new JButton();
        flush.setBounds(396, 74, 34, 20);
        flush.setText("刷新");
        flush.setMargin(new Insets(0, 0, 0, 0));
        flush.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
                repaint();
            }
        });
        panel.add(flush);

        JButton addNewUserButton = new JButton("");
        addNewUserButton.setText("增加用户");
        addNewUserButton.setBounds(445, 74, 60, 20);
        addNewUserButton.setMargin(new Insets(0, 0, 0, 0));
        addNewUserButton.addActionListener(new AddUserListener(this));
        panel.add(addNewUserButton);

        JButton PicArchiveButton = new JButton();
        PicArchiveButton.setText("图片库");
        PicArchiveButton.setBounds(520, 74, 60, 20);
        PicArchiveButton.setMargin(new Insets(0, 0, 0, 0));
        PicArchiveButton.addActionListener(new PicArchiveListener(this));
        panel.add(PicArchiveButton);




        if (showImgs == null) {

            return;
        }
        if (showImgs.size() == 0) {

//
        } else {
            int col = (showImgs.size() / 5) + 1;//行数
            int row = 5;//5列

            JLabel[] labelpics = new JLabel[showImgs.size()];
            JLabel[] labels = new JLabel[showImgs.size()];

            for (int i = 0; i < col; i++) {
                for (int j = 0; j < row; j++) {
                    int index = j + i * 5;
                    if (index >= showImgs.size()) {
                        break;
                    }


                    if (showImgs.get(index).getImg() == null) {

                    } else {
                        ImageIcon img = new ImageIcon(showImgs.get(index).getImg());//获取图像
                        img.setImage(img.getImage().getScaledInstance(80, 60, Image.SCALE_DEFAULT));
                        labelpics[index] = new JLabel();
                        labelpics[index].setBounds(37 + j * 118, 110 + i * 84, 80, 60);
                        labelpics[index].setIcon(img);
                        labelpics[index].addMouseListener(new LabelMouseListener(showImgs.get(index), labelpics[index], frame, this));
                        panel.add(labelpics[index]);
                    }

                    labels[index] = new JLabel();
                    labels[index].setBounds(37 + j * 118, 175 + i * 84, 90, 24);
                    labels[index].setText(showImgs.get(index).getName());
                    panel.add(labels[index]);
                }
            }
        }

    }

    public void addUser(User user, ServerImg img, RequestFinishListener listener) {
        try {
            mainApp.addUser(user, img.getUrl());
            showImgs = mainApp.getList();
            listener.log("添加用户成功");
        } catch (Exception e) {
            listener.error(e);
        }

//		frame.validate();
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

    public static void showMessage(String info) {
        JOptionPane.showMessageDialog(null, info, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(String info) {
        JOptionPane.showMessageDialog(null, info, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
