package Client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.ImgBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import entity.Data;
import entity.ServerImg;
import entity.ShowImg;
import entity.User;

public class AddUserListener implements ActionListener {
    private JFrame frame;
    private List<ServerImg> imgs = new ArrayList<>();
    private ServerImg selectedImage = null;
    public MainWindow mainWindow;
    private final int SUCCESS = 1;
    private final int FAILED = 0;


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub


        JFrame addUserFrame = new JFrame();
        imgs = mainWindow.mainApp.getImgs();

        addUserFrame.setPreferredSize(new Dimension(480, 370));

        addUserFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel UserNameLabel = new JLabel();
        UserNameLabel.setText("Username:");
        UserNameLabel.setBounds(20, 10, 97, 30);
        panel.add(UserNameLabel);

        JTextField UserName = new JTextField();
        UserName.setText("user");
        UserName.setBounds(97, 10, 196, 30);
        panel.add(UserName);


        JLabel UrlLabel = new JLabel();
        UrlLabel.setText("URL:");
        UrlLabel.setBounds(20, 56, 97, 30);
        panel.add(UrlLabel);

        JTextField Url = new JTextField();
        Url.setText("url");
        Url.setBounds(97, 56, 196, 30);
        panel.add(Url);

        //选择图片
        JLabel addPicLabel = new JLabel();
        addPicLabel.setBounds(20, 102, 97, 30);
        addPicLabel.setText("选择壁纸");
        panel.add(addPicLabel);

        //获取选中的图片
        if (imgs != null) {
            if (imgs.size() != 0) {
                selectedImage = imgs.get(0);
//                System.out.println("here");
                if (selectedImage == null) {
                    return ;
                }
            } else {
                return ;
            }
        } else {
            return ;
        }

        JLabel selectedPic = new JLabel();
        selectedPic.setBounds(97, 102, 40, 40);
        Image image = null;

        try {
            image = ImgBaseUtil.GetImageByByte(selectedImage.getData());
        } catch (Exception ex) {
            MainWindow.showWarning("操作失败");
            return ;
        }
        ImageIcon icon = new ImageIcon();
        icon.setImage(image.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        selectedPic.setIcon(icon);

        selectedPic.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {//单击图片触发事件
                int col = (imgs.size() / 5) + 1;//行数
                int row = 5;//5列

                JFrame selectPic = new JFrame();
                selectPic.setSize(480, 320);
                selectPic.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                JPanel showPic = new JPanel();
                showPic.setLayout(null);

                JLabel[] labelpics = new JLabel[imgs.size()];

                for (int i = 0; i < col; i++) {
                    for (int j = 0; j < row; j++) {
                        int index = j + i * 5;
                        if (index >= imgs.size()) {
                            break;
                        }


                        if (imgs.get(index) == null) {
                            MainWindow.showWarning("检测到错误信息");
                            return ;
                        } else {
                            Image image = null;
                            try {
                                image = ImgBaseUtil.GetImageByByte(imgs.get(index).getData());
                            } catch (Exception e) {
                                MainWindow.showWarning("操作失败");
                                return ;
                            }
                            ImageIcon img = new ImageIcon(image);//获取图像
                            img.setImage(img.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                            labelpics[index] = new JLabel();
                            labelpics[index].setBounds(37 + j * 118, 110 + i * 84, 80, 60);
                            labelpics[index].setIcon(img);
                            labelpics[index].addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    addUserFrame.invalidate();
                                    selectedImage = imgs.get(index);
                                    Image image = null;
                                    try {
                                        image = ImgBaseUtil.GetImageByByte(selectedImage.getData());
                                    } catch (Exception ex) {
                                        MainWindow.showWarning("操作失败");
                                        addUserFrame.validate();
                                        addUserFrame.repaint();
                                        selectPic.dispose();
                                    }
                                    ImageIcon ic = new ImageIcon(image);
                                    ic.setImage(image.getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                                    selectedPic.setIcon(ic);
                                    addUserFrame.validate();
                                    addUserFrame.repaint();
                                    selectPic.dispose();
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }
                            });
                            showPic.add(labelpics[index]);
                        }
                    }
                }

                selectPic.add(showPic);
                selectPic.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }
        });

        panel.add(selectedPic);


        JButton Confirm = new JButton();
        Confirm.setText("确认");
        Confirm.setBounds(97, 200, 50, 30);
        Confirm.setMargin(new Insets(0, 0, 0, 0));
        Confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = UserName.getText();
                String url = Url.getText();
                User user = new User();
                user.setName(userName);
                user.setUrl(url);
                ServerImg selectImage = selectedImage;

                mainWindow.addUser(user, selectImage, new RequestFinishListener() {
                    @Override
                    public void log(String response) {
                        MainWindow.showMessage(response);
                        LogBaseUtil.saveLog(SUCCESS, response);
                    }

                    @Override
                    public void error(Exception ex) {
                        MainWindow.showMessage(ex.getMessage());
                    }
                });

                addUserFrame.dispose();
                mainWindow.repaint();
            }
        });
        panel.add(Confirm);


        addUserFrame.add(panel);
        addUserFrame.pack();
        addUserFrame.setVisible(true);
    }


    public AddUserListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }


    public AddUserListener(JFrame frame, MainWindow mainWindow) {
        this.frame = frame;
        this.mainWindow = mainWindow;
    }

    public JFrame getFrame() {
        return frame;
    }


    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

}
