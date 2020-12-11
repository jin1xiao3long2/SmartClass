package Client.DownPic.utils.BaseUtils;

import com.jcraft.jsch.*;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.ListIterator;
import java.util.Properties;

public class SftpBaseUtil {

    private static final String REMOTE_HOST = "192.168.88.1";
    private static final String USERNAME = "jola";
    private static final String PASSWORD = "jxda7797797";
    private static final String REMOTE_PORT = "22";   //ssh协议默认端口
    private static final int SESSION_TIMEOUT = 60000; //session超时时间
    private static final int CHANNEL_TIMEOUT = 5000; //管道流超时时间
    static Session session = null;

    private static final String PATH = "/home/jol/wallpaper";
    private static final String NAME = "settings";

    public static ChannelSftp getConnectIP() {
        int port = 0;
        if (!("".equals(REMOTE_PORT)) && REMOTE_PORT != null) {
            port = Integer.parseInt(REMOTE_PORT);
        }
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
//            jsch.getSession(USERNAME, REMOTE_HOST, port);
            session = jsch.getSession(USERNAME, REMOTE_HOST, port);
            System.out.println("Session created");
            session.setPassword(PASSWORD);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.setTimeout(60000);//
            session.connect();
            System.out.println("session connected");
            System.out.println("opening channel");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  服务器储存目录
     * @param uploadFileName 上传文件路径
     * @param saveFileName    被保存的文件名
     * @param sftp       sftp
     * @return true --上传成功
     * false --上传失败
     */
    public static boolean upload(String directory, String uploadFileName, String saveFileName, ChannelSftp sftp) {
        boolean success = false;
        File file = new File(uploadFileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            sftp.cd(directory);
            sftp.put(fis, saveFileName);
            success = true;
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return success;
        }
        return success;
    }

    /**
     * 上传文件
     *
     * @param directory    目录
     * @param downloadFile 下载文件
     * @param saveFile     保存文件路径
     * @param sftp         sftp
     * @return true --下载成功
     * false --下载失败
     */
    public static boolean download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        boolean success = false;
        try {
            sftp.cd(directory);
            File file = new File(downloadFile);
            sftp.get(saveFile, new FileOutputStream(file));
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 查看
     * @param directory
     * @param sftp
     * @return
     */
    public static List<String> check(String directory, ChannelSftp sftp) {
        List<String> fileList = new ArrayList<>();
        try {
            sftp.cd(directory);
            ListIterator iter = sftp.ls(directory).listIterator();
            while (iter.hasNext()) {
                ChannelSftp.LsEntry eFile = (ChannelSftp.LsEntry) iter.next();
                String filename = eFile.getFilename();
                System.out.println(filename);
                fileList.add(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileList;
    }

    public static void disconnect(ChannelSftp sftp){
        if(sftp.isConnected()){
            session.disconnect();
            sftp.disconnect();
        }
    }

    public static boolean delete(String directory, String deleteFile, ChannelSftp sftp){
        boolean success = false;
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            success = true;
        }catch (Exception e){
            e.printStackTrace();
            return success;
        }
        return success;
    }


    public static void setServerData(){
        ;
    }

    public static void clearServerData(){
        ;
    }


}
