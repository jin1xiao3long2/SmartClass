package zrkc.group.new_ui.component.Listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClassInfoButtonCancelMouseListener implements MouseListener {

    JFrame frame = null;

    ClassInfoButtonCancelMouseListener(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        frame.setVisible(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        增加外边框
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        删除外边框
    }
}
