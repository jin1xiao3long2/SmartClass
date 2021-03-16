package zrkc.group.new_ui.component.Listener;

import zrkc.group.new_ui.component.MainFrame;
import zrkc.group.new_ui.component.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ClassTextMouseListener extends ClassImgMouseListener{

    JLabel text = null;

    public ClassTextMouseListener(MainPanel panel, JLabel t) {
        super(panel);
        text = t;
    }




}
