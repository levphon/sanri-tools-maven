package sanri.utils.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 * 
 * 创建时间:2017-8-13上午10:19:11<br/>
 * 创建者:sanri<br/>
 * 功能:二维码工具类<br/>
 */
public class QRCodeUtil extends ImageUtilBase {
	
	private static final ImageType imageType = ImageType.JPG;
	/**
	 * 
	 * 功能:生成二维码<br/>
	 * 创建时间:2017-8-13上午10:25:41<br/>
	 * 作者：sanri<br/>
	 * @param text
	 * @param width
	 * @param height
	 * @return<br/>
	 */
	public static BufferedImage generateImage(String text,int width,int height){
		QRCode qrCode = QRCode.from(text).withSize(width, height).withCharset("utf-8").to(imageType);
		ByteArrayOutputStream stream = qrCode.stream();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
		try {
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			IOUtils.closeQuietly(stream);
			IOUtils.closeQuietly(inputStream);
		}
		return null;
	}
	/**
	 * 
	 * 功能:生成二维码名片<br/>
	 * 创建时间:2017-8-13上午10:31:05<br/>
	 * 作者：sanri<br/>
	 * @param vCard
	 * @param width
	 * @param height
	 * @return<br/>
	 */
//	public static BufferedImage generateImage(VCard vCard,int width,int height){
//		ByteArrayOutputStream stream = QRCode.from(vCard).withSize(width, height).withCharset("utf-8").to(imageType).stream();
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
//		try {
//			BufferedImage bufferedImage = ImageIO.read(inputStream);
//			return bufferedImage;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally{
//			IOUtils.closeQuietly(stream);
//			IOUtils.closeQuietly(inputStream);
//		}
//		return null;
//	}
}
