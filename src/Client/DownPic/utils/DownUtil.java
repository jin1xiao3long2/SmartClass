package Client.DownPic.utils;

import Client.DownPic.listener.DownloadFinishListener;
import Client.DownPic.listener.RequestFinishListener;

import java.util.List;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownUtil {

    private static int FAILED = 0;
    private static int SUCCESS = 1;

    private static boolean check(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String name = sdf.format(date);
        File file1 = new File("D:\\JAVAprojects\\wallpapers\\logs" + name + ".txt");
        if(file1.exists())
            return true;
        return false;
    }

    public static void downWallpaper(){
        if(!check()){
            String requestAddress = "http://guolin.tech/api/bing_pic";
            //选择下载地址
            HttpUtil.doGet(requestAddress, new RequestFinishListener() {
                @Override
                public void finish(String response) {
                    downPhoto(response);
                }

                @Override
                public void error(Exception ex) {
                    CommUtil.saveLog(FAILED, "请求地址网址异常: " + ex.toString());
                }
            });
        }
    }

    public static void downPhoto(String picAddress){
        String path = "D:\\JAVAprojects\\SmartClass\\wallpapers";
        File saveDir = new File(path);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File[] allFiles = saveDir.listFiles();
        List<File> files = new ArrayList<File>();
        for(File file : allFiles){
            if(file.isFile()){
                files.add(file);
            }
        }
        if(files.size() == 0){
            String name = "1.jpg";
            HttpUtil.download(picAddress, path, name, new DownloadFinishListener() {
                @Override
                public void finish() {
                    CommUtil.saveLog(SUCCESS, "下载成功, 图片名为" + name);
                }

                @Override
                public void error(Exception ex) {
                    CommUtil.saveLog(FAILED, "下载异常: " + ex.toString());
                }
            });
        }else{
            int names[] = new int[files.size()];
            for(int i = 0; i < files.size(); i++){
                String fileName = files.get(i).getName();
                names[i] = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf(".")));
            }
            String name = CommUtil.getMax(names) + 1 + ".jpg";
            HttpUtil.download(picAddress, path, name, new DownloadFinishListener() {
                @Override
                public void finish() {
                    CommUtil.saveLog(SUCCESS, "下载成功, 图片名为" + name);
                }

                @Override
                public void error(Exception ex) {
                    CommUtil.saveLog(FAILED, "下载异常: " + ex.toString());
                }
            });
        }
    }
}
