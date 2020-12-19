package Client.DownPic.utils.BaseUtils;

import Client.DownPic.listener.RequestFinishListener;
import com.jcraft.jsch.*;
import entity.Server;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.ListIterator;
import java.util.Properties;

public class SftpBaseUtil {

    private static Server server = new Server();
    private static final Server DEFAULT_SERVER = new Server("192,168,88,1", "22", "jola", "jxda7797797", "/home/jola");
    private static final int SESSION_TIMEOUT = 5000; //session超时时间
    private static final int CHANNEL_TIMEOUT = 5000; //管道流超时时间
    static Session session = null;

    private static final int Log = 1;
    private static final int Ex = 0;


    public static ChannelSftp getConnectIP()throws Exception{
        int port = 0;
        String REMOTE_PORT = server.PORT;
        String REMOTE_HOST = server.HOST;
        String USERNAME = server.USERNAME;
        String PASSWORD = server.PASSWORD;
        if (!("".equals(REMOTE_PORT)) && REMOTE_PORT != null) {
            port = Integer.parseInt(REMOTE_PORT);
        }
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
//            jsch.getSession(USERNAME, REMOTE_HOST, port);
            session = jsch.getSession(USERNAME, REMOTE_HOST, port);
            System.out.println("正在连接服务器...");
            session.setPassword(PASSWORD);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.setTimeout(SESSION_TIMEOUT);//
            session.connect();
            System.out.println("连接成功");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
//            listener.log(USERNAME);
//            LogBaseUtil.saveLog(Log, "连接服务器 :" + USERNAME + " 成功");
        } catch (Exception e) {
            throw new Exception("服务器连接失败");
//            listener.error(e);
//            LogBaseUtil.saveLog(Log, "连接服务器失败, 异常信息请查看error文件");
//            LogBaseUtil.saveLog(Ex, "连接服务器 :" + USERNAME + " 异常:" + e.toString());
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory      服务器储存目录
     * @param uploadFileName 上传文件路径
     * @param saveFileName   被保存的文件名
     * @param sftp           sftp
     * @return true --上传成功
     * false --上传失败
     */
    public static void upload(String directory, String uploadFileName, String saveFileName, ChannelSftp sftp) throws Exception {
        File file = new File(uploadFileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            sftp.cd(directory);
            sftp.put(fis, saveFileName);
            fis.close();
        } catch (Exception e) {
            throw new Exception("上传文件失败");
        }
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
    public static void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) throws Exception {
        try {
            sftp.cd(directory);
            File file = new File(downloadFile);
            sftp.get(saveFile, new FileOutputStream(file));
        } catch (Exception e) {
            throw new Exception("下载文件失败");
        }
    }

    public static byte[] downloadBybyte(String directory, String downloadFile, String saveFile, ChannelSftp sftp) throws Exception {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        return fileData;
    }

    /**
     * 查看
     *
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
                fileList.add(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileList;
    }


    public static void disconnect(ChannelSftp sftp) {
        if (sftp.isConnected()) {
            session.disconnect();
            sftp.disconnect();
        }
    }

    public static void delete(String directory, String deleteFile, ChannelSftp sftp) throws Exception {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
//            listener.log(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
//            listener.error(e);
            throw new Exception("删除服务器文件失败");
        }
    }


    public static void setServerData(Server server) {
        setServerData(server.HOST, server.PORT, server.USERNAME, server.PASSWORD, server.HTTP_ROOT);
    }

    public static void setServerData(String remote_host, String remote_port, String username, String password, String path) {
        server.setHOST(remote_host);
        server.setPORT(remote_port);
        server.setUSERNAME(username);
        server.setPASSWORD(password);
        server.setHTTP_ROOT(path);
    }


    public static void clearServerData() {
        server = DEFAULT_SERVER;
    }


}
