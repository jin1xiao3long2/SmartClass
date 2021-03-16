package zrkc.group.new_ui.component.Listener;

import zrkc.group.new_ui.component.ClassInfoFrame;
import zrkc.group.new_ui.component.MainFrame;
import zrkc.group.new_ui.component.MainPanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public class ClassImgMouseListener implements MouseListener {

    protected MainFrame Frame = null;
    protected MainPanel Panel = null;
    protected Cursor Cu = null;
    protected ClassInfoFrame infoFrame = null;

    public ClassImgMouseListener(MainPanel panel, ){
        this.Panel = panel;
        this.Frame = panel.getMainFrame();
        Cu = Frame.getCursor();
        infoFrame = Panel.getChangeUserFrame();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        infoFrame.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Cu = new Cursor(Cursor.HAND_CURSOR);
        Frame.setCursor(Cu);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Cu = new Cursor(Cursor.DEFAULT_CURSOR);
        Frame.setCursor(Cu);
    }


}
