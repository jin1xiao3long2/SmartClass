package zrkc.group.new_ui.component;

import java.util.List;
import zrkc.group.javabean.ShowImg;

import javax.swing.JPanel;

public class ShowPanel extends JPanel {

    private List<ShowImg> showImgs = null;
    private int ShowImg_Width = 0;
    private int ShowImg_Height = 0;
    private int ImgScreen_Width_Interval = 0;
    private int ImgScreen_Height_Interval = 0;

    public ShowPanel(int x, int y, int w, int h, int s_w, int s_h, int s_w_i, int s_h_i){
        this.setLocation(x, y);
        this.setSize(w, h);
        this.ShowImg_Width = s_w;
        this.ShowImg_Height = s_h;
        this.ImgScreen_Width_Interval = s_w_i;
        this.ImgScreen_Height_Interval = s_h_i;
    }

    public void placeComponent(){
        if(showImgs == null)
            return ;
        if(showImgs.size() == 0)
            return ;
        int y = 0;
        for(int m = 0; m < 5; m++){
            if(m == 0 || m == 4){
                y += ImgScreen_Height_Interval;
            }else{
                y += ImgScreen_Height_Interval * 3;
            }
            int x = 0;
            for(int n = 0; n < 6; n++){
                if(n == 0 || n == 5){
                    x += ImgScreen_Width_Interval;
                }else{
                    x += ImgScreen_Width_Interval * 2;
                }

            }
        }
    }

    public void changeShowImgs(List<ShowImg> imgs){
        this.showImgs = imgs;
    }

    public List<ShowImg> getShowImgs(){
        return this.showImgs;
    }


}
