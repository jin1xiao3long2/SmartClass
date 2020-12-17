package Client.ui.test;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class test {

    @Test
    public void test1(){
        JFrame jFrame = new JFrame("hello");
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel();
        jFrame.setPreferredSize(new Dimension(800,600));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jPanel.setLayout(null);
        jLabel.setText("123");
        jPanel.add(jLabel);
        jPanel.setPreferredSize(new Dimension(600,400));
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jFrame.getContentPane().add(jScrollPane);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    @Test
    public void test2(){
        JFrame f = new JFrame("你好");//创建窗体对象

        f.setVisible(true);//设置窗体可见

        /*
         * EXIT_ON_CLOSE:隐藏窗口，并停止程序
         * DO_NOTHING_ON_CLOSE:无任何操作
         * HIDE_ON_CLOSE:隐藏窗体，但并不停止程序
         * DISPOSE_ON_CLOSE:释放窗体资源
         * */
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//设置窗体的关闭方式

//		f.setSize(500, 500);//设置窗体的大小  单位:像素
//		f.setLocation(500, 500);;//设置坐标  单位:像素
        f.setBounds(500, 500, 500, 500);//设置窗体坐标和大小  单位：像素

        Container  c = f.getContentPane();//获取窗体容器

        c.setBackground(Color.BLUE);//设置背景颜色

        JLabel l = new JLabel("这是一个窗体");//标签
        l.setHorizontalAlignment(SwingConstants.CENTER);//设置标签文字居中

        c.add(l);//添加组件
//		c.remove(l);//删除组件
        c.validate();//验证容器中的组件(类似刷新)
//		f.setContentPane(c);//重新载入容器

        f.setResizable(false);//设置窗体是否可以改变大小

        System.out.println("x="+f.getX()+" y="+f.getY());//获取窗体的坐标
    }

    public static void main(String[] args){
        JFrame jFrame = new JFrame("hello");
        JPanel jPanel = new JPanel();
        JButton addNewUserButton = new JButton("");
        addNewUserButton.setText("增加用户");
        addNewUserButton.setBounds(400,74,60,20);
        addNewUserButton.setMargin(new Insets(0,0,0,0));
        JButton delUserButton = new JButton("");
        delUserButton.setText("删除用户");
        delUserButton.setBounds(200,74,60,20);
        delUserButton.setMargin(new Insets(0,0,0,0));
        delUserButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jFrame.getContentPane().removeAll();
                jPanel.removeAll();
                jPanel.add(addNewUserButton);
                jFrame.getContentPane().add(new JScrollPane(jPanel));
                jFrame.invalidate();
                jFrame.validate();
                jFrame.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jFrame.setPreferredSize(new Dimension(800,600));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jPanel.setLayout(null);
        jPanel.add(addNewUserButton);
        jPanel.add(delUserButton);
        jPanel.setPreferredSize(new Dimension(600,400));
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jFrame.getContentPane().add(jScrollPane);
        jFrame.pack();
        jFrame.setVisible(true);

    }
}
