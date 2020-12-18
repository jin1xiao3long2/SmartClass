package Client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Client.DownPic.listener.RequestFinishListener;
import Client.DownPic.utils.BaseUtils.ImgBaseUtil;
import Client.DownPic.utils.BaseUtils.LogBaseUtil;
import entity.Server;
import entity.ServerImg;
import entity.ShowImg;

import javax.swing.*;

import entity.ShowImg;

public class LabelMouseListener implements MouseListener {//增加成员变量，重写构造函数实现传参
    private JLabel label;
    private ShowImg img;
    private JFrame f;
    private ServerImg selectedImage = null;
    public MainWindow mainWindow;
    private final int SUCCESS = 1;
    private final int FAILED = 0;

    public LabelMouseListener(ShowImg img, JLabel label, JFrame f, MainWindow mainWindow) {
        super();
        this.label = label;
        this.img = img;
        this.f = f;
        this.mainWindow = mainWindow;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {//单击
        // TODO Auto-generated method stub
        //		System.out.println(imgInfo.id);
        //		System.out.println(label.getName());
        JFrame frame = new JFrame();
        createChangePageFrame(frame);
    }

    public void createChangePageFrame(JFrame frame) {

        List<ServerImg> imgs = mainWindow.mainApp.getImgs();
        selectedImage = imgs.get(0);

        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel user = new JLabel("");
        user.setBounds(10, 27, 90, 24);
        System.out.println(label.getName());
        user.setText(label.getName());
        panel.add(user);

        JButton option = new JButton();
        option.setBounds(300, 27, 34, 28);
        option.setText("设置");
        option.setMargin(new Insets(0, 0, 0, 0));
        option.addActionListener(new SwitchToOptionListener(option.getText(), mainWindow));
        panel.add(option);

        JLabel label_1 = new JLabel("");
        label_1.setBounds(43, 245, 90, 24);
        label_1.setText("\u5F53\u524D\u58C1\u7EB8");
        panel.add(label_1);

        JLabel label_2 = new JLabel();
        label_2.addMouseListener(null);//mouseclick
        ImageIcon icon = new ImageIcon();//显示图片
        icon.setImage(img.getImg().getScaledInstance(116, 81, Image.SCALE_DEFAULT));
        label_2.setIcon(icon);
        label_2.setBounds(40, 147, 116, 81);
        panel.add(label_2);

        JLabel label_3 = new JLabel("");
        label_3.setBounds(424, 245, 90, 24);
        label_3.setText("\u9009\u62E9\u58C1\u7EB8");
        panel.add(label_3);

        JLabel label_21 = new JLabel();
        Image image = null;
        try {
            image = ImgBaseUtil.GetImageByByte(selectedImage.getData());
        } catch (Exception ex) {
            MainWindow.showWarning("操作失败");
            return;
        }
        ImageIcon img2 = new ImageIcon(image);

        img2.setImage(img2.getImage().getScaledInstance(116, 81, Image.SCALE_DEFAULT));
        label_21.setIcon(img2);
        label_21.setBounds(396, 147, 116, 81);

        label_21.addMouseListener(new MouseListener() {
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
                        if (index >= imgs.size() || imgs.get(index) == null) {

                        } else {
                            Image image = null;
                            try {
                                image = ImgBaseUtil.GetImageByByte(mainWindow.mainApp.getImgs().get(index).getData());
                            } catch (Exception ex) {
                                MainWindow.showWarning("错误信息");
                                return;
                            }
                            ImageIcon img = new ImageIcon(image);//获取图像
                            img.setImage(img.getImage().getScaledInstance(116, 81, Image.SCALE_DEFAULT));
                            labelpics[index] = new JLabel();
                            labelpics[index].setBounds(37 + j * 118, 110 + i * 84, 116, 81);
                            labelpics[index].setIcon(img);
                            labelpics[index].addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    Image image = null;

                                    frame.invalidate();
                                    selectedImage = imgs.get(index);
                                    try {
                                        image = ImgBaseUtil.GetImageByByte(selectedImage.getData());
                                    } catch (Exception ex) {
                                        MainWindow.showWarning("错误信息");
                                        frame.validate();
                                        frame.repaint();
                                        selectPic.dispose();
                                    }
                                    ImageIcon ic = new ImageIcon(image);
                                    ic.setImage(image.getScaledInstance(116, 81, Image.SCALE_DEFAULT));
                                    label_21.setIcon(ic);
                                    frame.validate();
                                    frame.repaint();
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

        panel.add(label_21);

        JButton button = new JButton();
        button.setBounds(300, 325, 80, 34);
        button.setText("返回");
        button.setMargin(new Insets(0, 0, 0, 0));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.dispose();
            }
        });

        panel.add(button);

        JButton del = new JButton();
        del.setText("删除");
        del.setBounds(200, 325, 80, 34);
        del.setMargin(new Insets(0, 0, 0, 0));
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//					mainWindow.delUser();
                //((JFrame)SwingUtilities.getWindowAncestor(f)).dispose();
			/*		JFrame F = new JFrame();
					F.setPreferredSize(new Dimension(566,410));
				    F.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					JPanel pane = new JPanel();

					new Scene1().placeComponentsHome(pane, imgs);
					JScrollPane scrollpane = new JScrollPane(pane);
				    scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				    scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					F.add(scrollpane);
					F.setVisible(true);
					F.pack();*/
                String username = img.getName();
                mainWindow.delUser(username, new RequestFinishListener() {
                    @Override
                    public void log(String response) {
                        MainWindow.showMessage(response);
                        LogBaseUtil.saveLog(SUCCESS, response);
                    }

                    @Override
                    public void error(Exception ex) {
                        MainWindow.showWarning(ex.getMessage());
                        LogBaseUtil.saveLog(FAILED, ex.getMessage());
                    }
                });
                mainWindow.repaint();
                frame.dispose();
            }
        });
        panel.add(del);

        JButton Confirm = new JButton();
        Confirm.setText("确认");
        Confirm.setBounds(100, 325, 80, 34);
        Confirm.setMargin(new Insets(0, 0, 0, 0));
        Confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //((JFrame)SwingUtilities.getWindowAncestor(f)).dispose();
			/*		JFrame F = new JFrame();
					F.setPreferredSize(new Dimension(566,410));
				    F.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					JPanel pane = new JPanel();
				      
					new Scene1().placeComponentsHome(pane, imgs);
					JScrollPane scrollpane = new JScrollPane(pane);
				    scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				    scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					F.add(scrollpane);
					F.setVisible(true);
					F.pack();*/
                String username = img.getName();
                mainWindow.changePaper(username, selectedImage, new RequestFinishListener() {
                    @Override
                    public void log(String response) {
                        MainWindow.showMessage(response);
                        LogBaseUtil.saveLog(SUCCESS, response);
                    }

                    @Override
                    public void error(Exception ex) {
                        MainWindow.showWarning(ex.getMessage());
                        LogBaseUtil.saveLog(FAILED, ex.getMessage());
                    }
                });
                mainWindow.repaint();
                frame.dispose();
            }
        });
        panel.add(Confirm);
        frame.add(panel);
        frame.setVisible(true);
			/*(new Scene1();
			Scene1.placeComponentsChangepic(panel);
			panel.setPreferredSize(new Dimension(500,500));
			JScrollPane scrollpane = new JScrollPane(panel);
			scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);*/
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {//单机
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }


}
