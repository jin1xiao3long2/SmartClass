package zrkc.group.utils.BaseUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImgBaseUtil {

    public static Image GetImageByByte(byte[] imageBytes) throws Exception {
        InputStream buffin = new ByteArrayInputStream(imageBytes);
        BufferedImage img = ImageIO.read(buffin);
        return img;
    }

    public static byte[] GetByteByImage(Image image) throws Exception{
        BufferedImage bu = new BufferedImage(image.getWidth(null), image.getHeight(null),BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            boolean result = ImageIO.write(bu, "jpg", bos);
        }catch (Exception e){
            throw new Exception("类型转换失败");
        }
        byte[] data = bos.toByteArray();
        return data;
    }
}
