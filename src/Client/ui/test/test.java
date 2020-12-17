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

    public static class Rec extends JPanel{
        private int x;
        private int y;
        private int w;
        private int h;

        public Rec(int x, int y, int w, int h){
            super();
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            g.fillRect(x,y,w,h);
        }
    }

    public static void main(String[] args){
        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setPreferredSize(new Dimension(700, 500));
        Rec rec = new Rec(30,40,50,60);
        rec.setLayout(null);
        rec.setFocusable(true);

        JButton text = new JButton();
        text.setText("haha");
        text.setBounds(337, 60, 50, 30);
        text.setMargin(new Insets(0, 0, 0, 0));
        jPanel.add(text);

        jFrame.setPreferredSize(new Dimension(800, 600));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(rec);
        jFrame.getContentPane().add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
