package sanri.utils.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
/**
 * 
 * 创建时间:2016-10-2下午12:13:06<br/>
 * 创建者:sanri<br/>
 * 功能:生成验证码工具<br/>
 */
public class VerifyCodeUtil extends ImageUtilBase{
	private static Random random = new Random();
	/**
	 * 输出指定验证码图片流
	 * @param width 验证码宽度 200
	 * @param height 验证码高度 80
	 * @param os 输出流
	 * @param code 验证码 ,建议不要弄难以辨认的字符 i,l,1,o,0 等
	 * 系统需要安装 Algerian 字体 
	 * 这个验证码比较漂亮,建议使用这个
	 */
	public static BufferedImage generateImage(int width, int height, String code){
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
				Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
				Color.PINK, Color.YELLOW };
		float[] fractions = new float[colors.length];
		for(int i = 0; i < colors.length; i++){
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);
		
		g2.setColor(Color.GRAY);// 设置边框色
		g2.fillRect(0, 0, width, height);
		
		Color c = getRandColor(200, 250);
		g2.setColor(c);// 设置背景色
		g2.fillRect(0, 2, width, height-4);
		
		//绘制干扰线
		Random random = new Random();
		g2.setColor(getRandColor(160, 200));// 设置线条的颜色
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
		
		// 添加噪点
		float yawpRate = 0.05f;// 噪声率
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}
		
		shear(g2, width, height, c);// 使图片扭曲

		g2.setColor(getRandColor(100, 160));
		int fontSize = height-4;
		Font font = new Font("Algerian", Font.ITALIC, fontSize);
		g2.setFont(font);
		char[] chars = code.toCharArray();
		for(int i = 0; i < verifySize; i++){
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (width / verifySize) * i + fontSize/2, height/2);
			g2.setTransform(affine);
			g2.drawChars(chars, i, 1, ((width-10) / verifySize) * i + 5, height/2 + fontSize/2 - 10);
		}
		
		g2.dispose();
		return image;
	}
	
	 /**
     * 生成验证码<br>
     * <b>注意：验证码图片宽度必须大于验证码文字的左边距，验证码图片高度必须大于验证码文字的上边距<b>
     * @param captchaWidth 验证码图片宽度
     * @param captchaHeight 验证码图片高度
     * @param textmarginLeft 验证码文字的左边距
     * @param textmarginTop 验证码文字的上边距
     * @param textSpacing 验证码文字的间距
     * @param groundColor 验证码图片背景颜色
     * @param borderColor 验证码边框颜色
     * @param textfont 验证码文字大小和样式
     * @param captchaText 验证文字
     * @param addNoise 是否加入干扰线
     * @param needBorder 是否加入边框
     * @return BufferedImage
     * @throws IllegalArgumentException
     */
    public static BufferedImage generateImage(int captchaWidth, int captchaHeight, int textmarginLeft,
            int textmarginTop, int textSpacing, Color groundColor, Color borderColor, Font textfont, String captchaText,
            boolean addNoise, boolean needBorder) throws IllegalArgumentException {
        if (captchaWidth < textmarginLeft) {
            throw new IllegalArgumentException("验证码图片宽度必须大于验证码文字的左边距");
        }
        if (captchaHeight < textmarginTop) {
            throw new IllegalArgumentException("验证码图片高度必须大于验证码文字的上边距");
        }
        
        // 在内存中创建图象
        BufferedImage image = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        
        // 设定背景色
        g.setColor(groundColor);
        g.fillRect(0, 0, captchaWidth, captchaHeight);
        // 设定字体
        g.setFont(textfont);

        if (needBorder) {
            // 画边框
            g.setColor(borderColor);
            g.drawRect(0, 0, captchaWidth - 1, captchaHeight - 1);
        }
        // 随机产生155条干扰线
        if (addNoise) {
            g.setColor(getRandColor(160, 200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(captchaWidth);
                int y = random.nextInt(captchaHeight);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
        }
        for (int i = 0; i < captchaText.length(); i++) {
            String rand = String.valueOf(captchaText.charAt(i));
            // 将验证码显示到图象中
            g.setColor(new Color(random.nextInt(220), random.nextInt(220), random.nextInt(220)));
            g.drawString(rand, textmarginLeft * i + textSpacing, textmarginTop);
        }
        g.dispose();

        return image;
    }

	
	
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}
	
	private static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}
	
	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}
	
	private static void shearX(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private static void shearY(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}
}
