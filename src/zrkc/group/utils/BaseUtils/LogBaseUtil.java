package zrkc.group.utils.BaseUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogBaseUtil {

//    private MainUser mainUser = null;
    private static String logPath = null;
    private static String errorPath = null;

    public static void init(String projectPath){
        logPath = projectPath + "\\logs";
        errorPath = projectPath + "\\errors";
        File logDir = new File(logPath);
        if(!logDir.exists())
            logDir.mkdir();
        File errorDir = new File(errorPath);
        if(!errorDir.exists())
            errorDir.mkdir();
    }


    /**
     *
     * @param state 1->log 0->error
     * @param message
     */
    public static void saveLog(int state, String message){
        FileOutputStream out = null;
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String name = sdf.format(date);
            File saveDir = null;
            if(state == 1){
                saveDir = new File(logPath);
            }else if(state == 0){
                saveDir = new File(errorPath);
            }
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            File file = new File(saveDir + "\\" + name + ".txt");
            if(!file.exists())
                file.createNewFile();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(date);
            String log = "时间:" + time + ", " + message;
            FileBaseUtil.appendlnFile(saveDir + "\\" + name + ".txt", log);
        }catch (Exception e){

        }
    }
}
