package Client.DownPic.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CommUtil {
    public static int getMax(int[] array){
        Arrays.sort(array);
        return array[array.length - 1];
    }

//    获取工程路径
    public static void saveLog(int state, String message){
        FileOutputStream out = null;
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String name = sdf.format(date);
            File saveDir = null;
            if(state == 1){
                saveDir = new File("D:\\JAVAprojects\\SmartClass\\wallpapers\\logs");
            }else if(state == 0){
                saveDir = new File("D:\\JAVAprojects\\SmartClass\\wallpapers\\errors");
            }
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            File file = new File(saveDir + "\\" + name + ".txt");
            out = new FileOutputStream(file, true);
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(date);
            String log = "时间:" + time + ", " + message + "\n";
            out.write(log.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
        }
    }
}
