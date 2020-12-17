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

    public static String readFile(String path) throws Exception {
        File file = new File(path);
        BufferedReader reader = null;
        String fileText = "";
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                fileText = fileText + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            throw new Exception("读取文件" + file + "失败");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return fileText;
    }

    /**
     * 文件写入字符串
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return
     * @throws Exception 抛出写入文件信息错误异常
     */
    public static void writeFile(String filePath, String text) throws Exception {
        try {
            writeFileBase(filePath, text, false, false);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 文件追加字符串
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return
     * @throws Exception 抛出写入文件信息错误异常
     */
    public static void appendFile(String filepath, String text) throws Exception {
        try {
            writeFileBase(filepath, text, true, false);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 文件追加字符串并换行
     *
     * @param filePath 路径名
     * @param text     写入的信息
     * @return
     * @throws Exception 抛出写入文件信息错误异常
     */
    public static void appendlnFile(String filepath, String text) throws Exception {

        try {
            writeFileBase(filepath, text, true, true);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String createFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                return filePath;
            } catch (Exception e) {
                throw new Exception("创建文件错误");
            }
        } else {
            throw new Exception(filePath + "文件已存在,请检查文件并进行删除");
        }
    }

    public static byte[] getBytesByFile(String pathStr) throws Exception {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            throw new Exception("文件" + pathStr + "读取失败");
        }
    }


    public static String deleteFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return filePath;
        } else {
            throw new Exception("该文件不存在");
        }
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
    private static void writeFileBase(String filePath, String text, boolean is_append, boolean is_newline) throws Exception {
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
        } catch (IOException e) {
//            e.printStackTrace();
            throw new Exception("写入文件错误");
        }
    }

}
