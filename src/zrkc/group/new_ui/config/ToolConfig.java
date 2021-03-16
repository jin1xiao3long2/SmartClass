package zrkc.group.new_ui.config;

import javax.swing.*;
import java.awt.*;

public class ToolConfig {
    private Cursor cu = null;
    private Toolkit tk = null;
    private JFrame mainFrame = null;

    public ToolConfig(){
        tk = Toolkit.getDefaultToolkit();
        cu = new Cursor(Cursor.DEFAULT_CURSOR);
    }

    public Cursor getCu() {
        return cu;
    }

    public void setCu(Cursor cu) {
        this.cu = cu;
    }

    public Toolkit getTk() {
        return tk;
    }

    public void setTk(Toolkit tk) {
        this.tk = tk;
    }
}
