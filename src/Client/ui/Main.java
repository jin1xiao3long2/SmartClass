package Client.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Client.DownPic.utils.DownUtil;
import Client.ui.Change.change;

public class Main {
    public static void main(String[] args) {
//        downloadTimer();
//        DownUtil.downWallpaper();
        change.change("D:\\JAVAProjects\\SmartClass\\wallpapers\\1.jpg");
        //增加为ui
    }

    public static void downloadTimer() {
        // 设置执行时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天
        Date date = calendar.getTime();
        calendar.set(year, month, day, 20, 16, 00);
        Timer timer = new Timer();
        // 第一次执行
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                DownUtil.downWallpaper();
            }
        };
        // 每天9:00时刻执行，每隔30分钟重复执行一次
        int peroid = 30 * 60 * 1000;
        timer.schedule(task, date, peroid);
    }
}