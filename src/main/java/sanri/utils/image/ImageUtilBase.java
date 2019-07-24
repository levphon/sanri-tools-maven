package sanri.utils.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public abstract class ImageUtilBase {
	/**
	 * 
	 * 功能:图片写入输出流<br/>
	 * 创建时间:2017-8-5下午8:32:37<br/>
	 * 作者：sanri<br/>
	 * @param image 图片对象
	 * @param os 输出流
	 * @throws IOException<br/>
	 */
	public static void writeImage(BufferedImage image,OutputStream os) throws IOException{
		ImageIO.write(image, "jpg", os);
		if(os != null){
			os.close();
		}
	}
	
	/**
	 * 
	 * 功能:输出图片到文件<br/>
	 * 创建时间:2017-8-5下午8:34:19<br/>
	 * 作者：sanri<br/>
	 * @param image
	 * @param destFile
	 * @throws IOException<br/>
	 */
	public static void wirteImage2File(BufferedImage image,File destFile) throws IOException{
		FileOutputStream fileOutputStream = new FileOutputStream(destFile);
		writeImage(image, fileOutputStream);
	}
	
}
