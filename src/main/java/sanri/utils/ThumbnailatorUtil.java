package sanri.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author Administrator
 * @Title ThumbnailatorUtil.java
 * @Package com.hhx.tiger.util.image
 * @date 2018年6月27日 下午3:25:37
 */
public class ThumbnailatorUtil {

    private static Logger logger = Logger.getLogger(ThumbnailatorUtil.class);//日志记录

    /**
     * 将图片压缩到1.2M以下
     *
     * @param filePath
     * @param filename
     * @param destFilename
     */
    public static void compressImage(String filePath, String filename, String destFilename) {
        try {
            double outputQuality = 1.0d;
            File file = new File(filePath + File.separator + filename);
            long fileSize = file.length();
            if (fileSize > 1.2 * 1024 * 1024) {
                if (fileSize >= 10 * 1024 * 1024) {
                    outputQuality = 0.1d;
                } else if (8 * 1024 * 1024 <= fileSize && fileSize < 10 * 1024 * 1024) {
                    outputQuality = 0.20d;
                } else if (7 * 1024 * 1024 <= fileSize && fileSize < 8 * 1024 * 1024) {
                    outputQuality = 0.25d;
                } else if (6 * 1024 * 1024 <= fileSize && fileSize < 7 * 1024 * 1024) {
                    outputQuality = 0.50d;
                } else if (5 * 1024 * 1024 <= fileSize && fileSize < 6 * 1024 * 1024) {
                    outputQuality = 0.60d;
                } else if (4 * 1024 * 1024 <= fileSize && fileSize < 5 * 1024 * 1024) {
                    outputQuality = 0.35d;
                } else if (3 * 1024 * 1024 <= fileSize && fileSize < 4 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (2 * 1024 * 1024 <= fileSize && fileSize < 3 * 1024 * 1024) {
                    outputQuality = 0.65d;
                } else if (1.2 * 1024 * 1024 <= fileSize && fileSize < 2 * 1024 * 1024) {
                    outputQuality = 0.75d;
                }
                Thumbnails.of(file)
                        .scale(1.0d)
                        .outputQuality(outputQuality)
                        .toFile(new File(filePath + File.separator + destFilename));
            }

        } catch (Exception e) {
            logger.error("图片压缩异常" + e.getMessage(), e);
        }
    }
}
