package sanri.utils.image;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

/**
 * 
 * 创建时间:2016年5月30日下午2:44:51<br/>
 * 创建人 :sanri<br/>
 * 功能 :这里只是裁剪后的图片的配置,原始的图片信息在裁剪工具类中<br/>
 */
public class ImageFileConfig {
	// 剪裁图片的 key
	public final static String CUT = "cut";
	// 原图 key
	public final static String NATURE = "nature";
	// 缩放图片 key 前缀,后面依次是 0,1,2....代表第几张缩小的图
	public final static String ZOOMPREFIX = "size_";
	// 旋转后的图
	public final static String ROTATE = "rotate";

	// 从哪个位置开始裁剪
	private int x;
	private int y;
	// 裁剪宽度,高度
	private int width;
	private int height;
	// 缩放比例
	private double aspectRatio;
	// 加原图一起总共多少张图片,这只是对于缩放图片来说的,缩放出三张图加原图一起四张的话, picNum = 2 也就是三张图
	private int picNum;
	// 旋转角度
	private int rotate;
	// 角度对应的弧度
	private double radian;
	// 新生成的文件后缀,开始是默认是原图后缀
	private ImageType suffix;
	// 是否保存原图
	private boolean isSaveNature;

	private PicNameRenamePolicy renamePolicy;
	private PathPolicy pathPolicy;

	public ImageFileConfig() {

	}

	// 需生成的图片类型
	public enum ImageType {
		JPG(0, "JPG"), GIF(1, "GIF"), PNG(2, "PNG");

		private int value;
		private String suffix;

		private ImageType(int value, String suffix) {
			this.value = value;
			this.suffix = suffix;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getSuffix() {
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}

		public static ImageType findTypeBySuffix(String suffix) throws IllegalArgumentException {
			ImageType[] values = ImageType.values();
			for (ImageType imageType : values) {
				if (imageType.getSuffix().equalsIgnoreCase(suffix)) {
					return imageType;
				}
			}
			throw new IllegalArgumentException("不支持的类型:" + suffix);
		}
	}

	/**
	 * 
	 * 创建时间:2016年5月30日下午3:17:33<br/>
	 * 创建人 :sanri<br/>
	 * 功能 :图片重命名配置<br/>
	 */
	public interface PicNameRenamePolicy {
		/**
		 * 
		 * 功能：这里只生成图片的名字,后缀不用管, 这里的 key 需要和前面生成图片的 key 对应<br/>
		 * 输入参数：当前图片张数,<br/>
		 * 输出参数：图片索引对应新图片名称 <br/>
		 * 作者:sanri<br/>
		 * 创建时间：2016年5月30日下午2:01:41<br/>
		 */
		String handler(String mark);
	}

	/**
	 * 
	 * 创建时间:2016年5月30日下午3:17:16<br/>
	 * 创建人 :sanri<br/>
	 * 功能 :生成图片路径配置<br/>
	 */
	public interface PathPolicy {
		File handler(String mark);
	}

	/**
	 * 
	 * 创建时间:2016-5-31下午9:14:20<br/>
	 * 创建者:sanri<br/>
	 * 功能:这个描述了原图,裁剪的,缩放的图片的命名策略<br/>
	 */
	public class DefaultReNamePolicy implements PicNameRenamePolicy {
		@Override
		public String handler(String mark) {
			return mark + UUID.randomUUID().toString();
		}
	}

	/**
	 * 
	 * 创建时间:2016-5-31下午9:13:48<br/>
	 * 创建者:sanri<br/>
	 * 功能:这个接口用来描述原图,裁剪的,缩放的图片分别存放在哪<br/>
	 * 采用日期三级目录加时间 hash 两级目录 ,一般能满足大部分需求
	 */
	public class DefaultPathPolicy implements PathPolicy {
		@Override
		public File handler(String mark) {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int date = cal.get(Calendar.DATE);
			int timeHash = String.valueOf(System.currentTimeMillis()).hashCode();
			int dir1 = timeHash & 0xf; // 0--15
			int dir2 = (timeHash & 0xf0) >> 4; // 0-15
			File finalDestDir = new File(System.getProperty("user.dir"), year + File.separator + month + File.separator + date+File.separator+dir1+File.separator+dir2);
			if (!finalDestDir.exists()) {
				finalDestDir.mkdirs();
			}
			File strogeDir = new File(finalDestDir, mark);
			if (!strogeDir.exists()) {
				strogeDir.mkdir();
			}
			return strogeDir;
		}

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(double aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public int getPicNum() {
		return picNum;
	}

	public void setPicNum(int picNum) {
		this.picNum = picNum;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
		this.radian = Math.toRadians(rotate);
	}

	public double getRadian() {
		return radian;
	}

	public void setRadian(double radian) {
		this.radian = radian;
		this.rotate = (int) Math.toDegrees(radian);
	}

	public ImageType getSuffix() {
		return suffix;
	}

	public void setSuffix(ImageType suffix) {
		this.suffix = suffix;
	}

	public PicNameRenamePolicy getRenamePolicy() {
		return renamePolicy;
	}

	public void setRenamePolicy(PicNameRenamePolicy renamePolicy) {
		this.renamePolicy = renamePolicy;
	}

	public PathPolicy getPathPolicy() {
		return pathPolicy;
	}

	public void setPathPolicy(PathPolicy pathPolicy) {
		this.pathPolicy = pathPolicy;
	}

	public boolean isSaveNature() {
		return isSaveNature;
	}

	public void setSaveNature(boolean isSaveNature) {
		this.isSaveNature = isSaveNature;
	}
}
