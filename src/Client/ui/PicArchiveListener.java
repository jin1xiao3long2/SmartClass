package Client.ui;

import Client.DownPic.utils.BaseUtils.ImgBaseUtil;
import entity.ServerImg;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PicArchiveListener extends JFrame implements ActionListener {
    private List<ServerImg> imgs = new ArrayList<>();
    MainWindow mainWindow = null;
    JFrame frame = new JFrame("");
    Rec panel = new Rec();
    ServerImg selectImg = null;


    public class Rec extends JPanel {
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean inited = false;

        public Rec() {
            super();
        }

        private void addRec(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            inited = true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (inited) {
                g.setColor(Color.BLUE);
                g.fillRect(x, y, width, height);
            }
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        frame.getContentPane().removeAll();
        panel.removeAll();
        panelDraw();
        frame.getContentPane().add(panel);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    void panelDraw() {
        System.out.println("imgs size is " + imgs.size());

        frame.setPreferredSize(new Dimension(566, 410));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.setLayout(null);

        JButton uploadButton = new JButton();
        uploadButton.setText("上传图片");
        uploadButton.setBounds(37, 60, 60, 40);
        uploadButton.setMargin(new Insets(0, 0, 0, 0));

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg,png", "jpg");
                chooser.setFileFilter(filter);
                int returnval = chooser.showOpenDialog(uploadButton);
                if (returnval == JFileChooser.APPROVE_OPTION) {
                    File[] arrfiles = chooser.getSelectedFiles();
                    if (arrfiles == null || arrfiles.length == 0) {
                        System.out.println("length=0 or cannot get file");
                        return;
                    }
                    File ff = chooser.getSelectedFile();
                    String fileName = ff.getName();
                    String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);//extends name like jpg/png
                    if (!(prefix.equals("jpg") || prefix.equals("png"))) {
                        JOptionPane.showMessageDialog(new JDialog(), "仅支持上传jpg与png图片", "警告", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String absolutePath = chooser.getSelectedFile().getAbsolutePath();
                    ImageIcon imageicon = new ImageIcon(absolutePath);
                    ServerImg img = new ServerImg();
                    String url = mainWindow.uploadPic(absolutePath);
                    img.setUrl(url);
                    try {
                        img.setData(ImgBaseUtil.GetByteByImage(imageicon.getImage()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    imgs.add(img);
                    JOptionPane.showMessageDialog(new JDialog(), "上传成功");
                    repaint();

                }
            }
        });

        panel.add(uploadButton);

        if (imgs == null) {
            System.out.println("NULL IMGS");
            return;
        } else if (imgs.size() == 0) {
            System.out.println("0 IMG");
            return;
        }

        int col = (imgs.size() / 5) + 1;//����
        int row = 5;

        JLabel[] labelpics = new JLabel[imgs.size()];

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                int index = j + i * 5;
                if (index >= imgs.size()) {
                    break;
                }


                if (imgs.get(index) == null) {

                } else {

                    if (selectImg == imgs.get(index)) {
                        panel.addRec(35 + j * 118, 108 + i * 84, 84, 64);
                        System.out.println("new rec");
                    }

                    Image image = null;
                    try {
                        image = ImgBaseUtil.GetImageByByte(imgs.get(index).getData());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ImageIcon img = new ImageIcon(image);
                    img.setImage(img.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                    labelpics[index] = new JLabel();
                    labelpics[index].setBounds(37 + j * 118, 110 + i * 84, 80, 60);
                    labelpics[index].setIcon(img);

                    labelpics[index].addMouseListener(new MouseListener() {

                                                          @Override
                                                          public void mouseClicked(MouseEvent arg0) {
                                                              selectImg = imgs.get(index);
                                                              repaint();
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
                                                      }
                    );
                    panel.add(labelpics[index]);
                }
            }
        }

//		del
        JButton del = new JButton();
        del.setText("删除");
        del.setBounds(337, 60, 50, 30);
        del.setMargin(new Insets(0, 0, 0, 0));
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectImg != null) {
                    if (mainWindow.has_defaultImg()) {
                        mainWindow.delPic(selectImg.getUrl());
                        imgs.remove(selectImg);
                        JOptionPane.showMessageDialog(null, "删除图片成功","消息提示", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "请设置默认图片再进行修改", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请选择图片", "警告", JOptionPane.WARNING_MESSAGE);
                }
                mainWindow.repaint();
                repaint();
            }
        });
        panel.add(del);

        //		del
        JButton def = new JButton();
        def.setText("设置为默认图片");
        def.setBounds(187, 60, 120, 30);
        def.setMargin(new Insets(0, 0, 0, 0));
        def.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectImg == null) {
                    JOptionPane.showMessageDialog(null, "请选择图片", "警告", JOptionPane.WARNING_MESSAGE);
                } else {
                    mainWindow.setDefaultImg(selectImg.getUrl());
                    JOptionPane.showMessageDialog(null, "设置成功", "消息提示", JOptionPane.INFORMATION_MESSAGE);
                }
                repaint();
            }
        });
        panel.add(def);

        JButton ret = new JButton();
        ret.setText("返回");
        ret.setBounds(420, 60, 50, 30);
        ret.setMargin(new Insets(0, 0, 0, 0));
        ret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.invalidate();
                frame.dispose();
                mainWindow.repaint();
            }
        });

        panel.add(ret);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        imgs = mainWindow.getImgs();
        panelDraw();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    public PicArchiveListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

}
