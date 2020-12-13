package Client.DownPic.utils.BaseUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBaseUtil {


    /**
     * 获取文本内容
     *
     * @param path 路径名
     * @return str --json所有信息
     */

    public static String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                laststr = laststr + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

    /**
     * 字符串写入文件
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return true  --成功
     * flase --失败
     */
    public static boolean writeFile(String filePath, String text) {
        return writeFileBase(filePath, text, false, false);
    }

    /**
     * 字符串追加文件
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return true  --成功
     * flase --失败
     */
    public static boolean appendFile(String filepath, String text) {
        return writeFileBase(filepath, text, true, false);
    }

    /**
     * 字符串写入文件并换行
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return true  --成功
     * flase --失败
     */
    public static boolean appendlnFile(String filepath, String text) {
        return writeFileBase(filepath, text, true, true);
    }

    /**
     * 写文件base
     *
     * @param filePath   文件名
     * @param text       写入字符
     * @param is_append  是否为追加模式
     * @param is_newline 是否换行
     * @return
     */
    private static boolean writeFileBase(String filePath, String text, boolean is_append, boolean is_newline) {
        File file = null;
        try {
            file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file, is_append);
            StringBuffer sb = new StringBuffer(text);
            if (is_newline)
                sb.append("\r\n");
            out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
