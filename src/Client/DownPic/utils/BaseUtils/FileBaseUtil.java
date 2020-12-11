package Client.DownPic.utils.BaseUtils;

import java.io.*;

public class FileBaseUtil {


    /**
     * 获取文本内容
     * @param path 路径名
     * @return
     *       str --json所有信息
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
        System.out.println("Laststr is: " + laststr);
        return laststr;
    }

    /**
     * json字符串写入文件
     * @param filePath 路径名
     * @param sets      写入的信息
     * @return
     *       true  --成功
     *       flase --失败
     */
    public static boolean writeFile(String filePath, String sets) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath);
            PrintWriter out = new PrintWriter(fw);
            out.write(sets);
            out.println();
            out.close();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
