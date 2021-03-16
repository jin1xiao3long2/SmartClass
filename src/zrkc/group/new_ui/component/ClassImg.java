package zrkc.group.new_ui.component;

import zrkc.group.javabean.ShowImg;
import zrkc.group.javabean.User;
import zrkc.group.new_ui.component.Listener.ClassImgMouseListener;

import javax.swing.*;
import java.awt.*;

//需要将ClassInfoFrame声明之后再声明
public class ClassImg extends JPanel {

    private ShowImg showImg = null;
    private User user = null;
    private JLabel text = null;
    private JLabel image = null;
    private MainPanel mainPanel = null;
    int x;
    int y;
    int w;
    int h;

    public ClassImg(int x, int y, int w, int h, int i_w, int i_h, ShowImg img, User user, MainPanel panel){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.setLayout(null);
        this.setBounds(x, y, w, h + i_h);
        this.showImg = img;
        this.user = user;
        this.mainPanel = panel;
        addImage();
        addText(i_w, i_h);
    }


    private void addImage(){
        image = new JLabel();
        if(showImg == null || showImg.getImg() == null)
            System.out.println("no");
        System.out.println("y");
        ImageIcon icon = new ImageIcon(showImg.getImg());
        icon.setImage(icon.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
        image.setBounds(0,0, w, h);
        image.setIcon(icon);
        image.addMouseListener(new ClassImgMouseListener(mainPanel));
        this.add(image);
    }

    private void addText(int i_w, int i_h){

        String name = user.getName();
        text = new JLabel();
        text.setText(name);
        Font font = new Font("黑体", Font.PLAIN, i_h);
        text.setFont(font);
        text.setForeground(Color.BLUE);

        text.setBounds(get_x(i_h, name), h, i_w*name.length(),i_h);//位置需要居中
        text.addMouseListener(new ClassImgMouseListener(mainPanel));
        this.add(text);
    }

    private int get_x(int i_h, String name){
        int length = w;
        int middle = w / 2;
        int len = 0;
        char ch;
        for(int i = 0; i < name.length(); i++){
            ch = name.charAt(i);
            if(isChinese(ch))
            {
                len += 2;
                continue;
            }
            len += 1;
        }
        int word_len = len * i_h;
        int x = middle - word_len / 4;
        return x;
    }

    private boolean isChinese(char ch){
        try{
            return String.valueOf(ch).getBytes("UTF-8").length > 1;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
