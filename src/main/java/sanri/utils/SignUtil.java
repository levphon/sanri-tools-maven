package sanri.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Consts;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.UUID;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-21下午6:01:14<br/>
 * 功能:签名工具类 <br/>
 */
public final class SignUtil {

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-8-21下午6:02:45<br/>
	 * 功能: 生成签名字符串, 将给定的参数按照字典序排序,然后生成 sha1 签名<br/>
	 * @param values
	 * @return
	 */
	public static String signature(String... values){
		if(values == null || values.length == 0){
			throw new IllegalArgumentException("生成签名时,参数异常,没有参数");
		}
		//按照字典序排序
		Arrays.sort(values);
		StringBuffer list = new StringBuffer();
		for(String value:values){
			list.append(value);
		}
		//sha 加密
		String signature = DigestUtils.shaHex(list.toString());
		return signature;
	}
	
	/**
	 * 
	 * 功能:jdk 的 uuid 字符串<br/>
	 * 创建时间:2017-9-1下午9:45:34<br/>
	 * 作者：sanri<br/>
	 * @return<br/>
	 */
	public static String uuid(){
		return UUID.randomUUID().toString();
	}
	/**
	 * 
	 * 功能:jdk 生成的 uuid 有四个 - ,去掉 四个 - 生成 32 位的 uuid<br/>
	 * 创建时间:2017-9-1下午9:47:02<br/>
	 * 作者：sanri<br/>
	 * @return<br/>
	 */
	public static String uuid32(){
		return uuid().replace("-", "");
	}
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-9-4下午7:47:06<br/>
	 * 功能:返回永不重复的时间戳 <br/>
	 * @return
	 */
	public static synchronized String uniqueTimestamp(){
		return String.valueOf(System.currentTimeMillis());
	}
	public  static synchronized String uniqueString(long length){
		String nowTimestamp = String.valueOf(System.currentTimeMillis());
		if(length < nowTimestamp.length()){
			throw new IllegalArgumentException("无法生成长度小于 "+length+" 的唯一字符串,最小["+nowTimestamp.length()+"]");
		}
		if(length > nowTimestamp.length()){
			String randomString = RandomUtil.randomNumeric((int)(length - nowTimestamp.length()));
			return nowTimestamp + randomString;
		}
		return nowTimestamp;
	}
	
	public static final Charset CHARSET = Consts.UTF_8;
	
	/**
	 * 
	 * 功能:将给定字符串以 base64 使用 utf-8 编码成 base64 字符串<br/>
	 * 创建时间:2017-9-2下午4:40:41<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @return<br/>
	 */
	public static String encodeBase64(String source){
		byte[] bytes = source.getBytes(CHARSET);
		byte[] encodeBase64 = Base64.encodeBase64(bytes);
		return new String(encodeBase64);
	}
	
	/**
	 * 
	 * 功能:将给定 base64 字符串,以 utf-8 的 base64 解码<br/>
	 * 创建时间:2017-9-2下午4:42:55<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @return<br/>
	 */
	public static String decodeBase64(String source){
		byte[] bytes = source.getBytes(CHARSET);
		byte[] decodeBase64 = Base64.decodeBase64(bytes);
		return new String(decodeBase64);
	}
	
	/**
	 * 
	 * 功能:感觉和 base64 差不多,原理是:<br/>
	 * hex 编码原理 取 1 字节 的前4 位和后 4 位在 DIGITS 表中的映射,可自定义 digits 表,一般用数字和字母的组合 <br/>
	 * 4bit 编码成一个字母,"汉字" utf-8 编码总共 6 字节,所以最终生成 12 位<br/>
	 * 可以把字节数组变字符数组,变成可读的字符数组 <br/>
	 * 创建时间:2017-9-2下午4:49:11<br/>
	 * 作者：sanri<br/>
	 * @param bytes
	 * @return<br/>
	 */
	public static String encodeHex(byte [] bytes){
		char[] encodeHex = Hex.encodeHex(bytes);
		return new String(encodeHex);
	}
	
	/**
	 * 
	 * 功能:解码 hex<br/>
	 * 创建时间:2017-9-2下午4:54:12<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @return
	 * @throws DecoderException<br/>
	 */
	public static byte[] decodeHex(String source) throws DecoderException{
		char[] charArray = source.toCharArray();
		byte[] decodeHex = Hex.decodeHex(charArray);
		return decodeHex;
	}
	

	/**
	 * 
	 * 功能:使用 des 加密 <br/>
	 * 创建时间:2017-9-2下午5:06:42<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @param key
	 * @return<br/>
	 */
	public static String encryptDES(String source,String key){
		SecureRandom secureRandom = new SecureRandom();
		try {
			byte[] keyBytes = getDesKey(key.getBytes(CHARSET));
			// 由于 windows 默认是 gbk 编码,为避免造成调试时的不一致,强制使用为 utf-8
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			//创建密钥工厂把 desKeySpec 转换成 安全键
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,secureRandom);
			
			byte[] encryptSource = cipher.doFinal(source.getBytes(CHARSET));
			//转换为 hex 字符串
			String encodeHex = encodeHex(encryptSource);
			return encodeHex;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * 功能:使用 des 解密<br/>
	 * 创建时间:2017-9-2下午5:21:11<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @param key
	 * @return<br/>
	 */
	public static String decryptDES(String source,String key){
		SecureRandom secureRandom = new SecureRandom();
		try {
			// 由于 windows 默认是 gbk 编码,为避免造成调试时的不一致,强制使用为 utf-8
			byte[] keyBytes = getDesKey(key.getBytes(CHARSET));
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			//创建密钥工厂把 desKeySpec 转换成 安全键
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey,secureRandom);
			
			byte[] decodeHex = decodeHex(source);
			byte[] encryptSource = cipher.doFinal(decodeHex);
			return new String(encryptSource,CHARSET);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 
	 * 功能:md5 编码<br/>
	 * 创建时间:2017-9-2下午4:45:24<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @return<br/>
	 */
	public static String md5Hex(String source){
		return DigestUtils.md5Hex(source);
	}
	
	/**
	 * 
	 * 功能:sha 编码<br/>
	 * 创建时间:2017-9-2下午4:45:47<br/>
	 * 作者：sanri<br/>
	 * @param source
	 * @return<br/>
	 */
	public static String shaHex(String source){
		return DigestUtils.shaHex(source);
	}
	/**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * 
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private static byte[] getDesKey(byte[] arrBTmp) {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return arrB;
    }
}
