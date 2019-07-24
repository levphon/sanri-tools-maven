package learntest.httpclient;
 
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
/**
 * 保持同一session的HttpClient工具类
 * @author zhangwenchao
 *
 */
public class HttpClientKeepSession {
 
		private static final Logger LOG = LogManager.getLogger(HttpClient.class);
		public  static CloseableHttpClient httpClient = null;
		public  static HttpClientContext context = null;
		public  static CookieStore cookieStore = null;
		public  static RequestConfig requestConfig = null;
 
		static {
			init();
		}
 
		private static void init() {
			context = HttpClientContext.create();
			cookieStore = new BasicCookieStore();
			// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
			requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
					       .setConnectionRequestTimeout(60000).build();
			// 设置默认跳转以及存储cookie
			httpClient = HttpClientBuilder.create()
					     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
					     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
					     .setDefaultCookieStore(cookieStore).build();
		}
 
		/**
		 * http get
		 * 
		 * @param url
		 * @return response
		 * @throws ClientProtocolException
		 * @throws IOException
		 */
		public static CloseableHttpResponse get(String url) throws ClientProtocolException, IOException {
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpget, context);
			try {
				cookieStore = context.getCookieStore();
				List<Cookie> cookies = cookieStore.getCookies();
				for (Cookie cookie : cookies) {
					LOG.debug("key:" + cookie.getName() + "  value:" + cookie.getValue());
				}
			} finally {
				response.close();
			}
			return response;
		}
 
		/**
		 * http post
		 * 
		 * @param url
		 * @param parameters
		 *            form表单
		 * @return response
		 * @throws ClientProtocolException
		 * @throws IOException
		 */
		public static CloseableHttpResponse post(String url, String parameters)
				throws ClientProtocolException, IOException {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = toNameValuePairList(parameters);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpClient.execute(httpPost, context);
			try {
				cookieStore = context.getCookieStore();
				List<Cookie> cookies = cookieStore.getCookies();
				for (Cookie cookie : cookies) {
					LOG.debug("key:" + cookie.getName() + "  value:" + cookie.getValue());
				}
			} finally {
				response.close();
			}
			return response;
			
		}
		
		public static void upload(String url) {
			try {
				HttpPost httppost = new HttpPost(url);
				FileBody bin = new FileBody(new File("C:\\Users\\zhangwenchao\\Desktop\\jinzhongzi.jpg"));	
				HttpEntity reqEntity = MultipartEntityBuilder.create()
						.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
						.addPart("uploadFile", bin)
						.setCharset(CharsetUtils.get("UTF-8")).build();
				httppost.setEntity(reqEntity);
				System.out.println("executing request: "+ httppost.getRequestLine());
				CloseableHttpResponse response = httpClient.execute(httppost,context);
				try {
					cookieStore = context.getCookieStore();
					List<Cookie> cookies = cookieStore.getCookies();
					for (Cookie cookie : cookies) {
						LOG.debug("key:" + cookie.getName() + "  value:" + cookie.getValue());
					}
					
					System.out.println("----------------------------------------");
					System.out.println(response.getStatusLine());
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						// 响应长度
						System.out.println("Response content length: "
								+ resEntity.getContentLength());
						// 打印响应内容
						System.out.println("Response content: "
								+ EntityUtils.toString(resEntity));
					}
					// 销毁
					EntityUtils.consume(resEntity);
				} finally {
					response.close();
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		
		
 
		@SuppressWarnings("unused")
		private static List<NameValuePair> toNameValuePairList(String parameters) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			String[] paramList = parameters.split("&");
			for (String parm : paramList) {
				int index = -1;
				for (int i = 0; i < parm.length(); i++) {
					index = parm.indexOf("=");
					break;
				}
				String key = parm.substring(0, index);
				String value = parm.substring(++index, parm.length());
				nvps.add(new BasicNameValuePair(key, value));
			}
			System.out.println(nvps.toString());
			return nvps;
		}
		/**
		 * 手动增加cookie
		 * @param name
		 * @param value
		 * @param domain
		 * @param path
		 */
		public static void addCookie(String name, String value, String domain, String path) {
			BasicClientCookie cookie = new BasicClientCookie(name, value);
			cookie.setDomain(domain);
			cookie.setPath(path);
			cookieStore.addCookie(cookie);
		}
		/**
		 * 把结果console出来
		 * 
		 * @param httpResponse
		 * @throws ParseException
		 * @throws IOException
		 */
		public static void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();
			// 响应状态
			System.out.println("status:" + httpResponse.getStatusLine());
			System.out.println("headers:");
			HeaderIterator iterator = httpResponse.headerIterator();
			while (iterator.hasNext()) {
				System.out.println("\t" + iterator.next());
			}
			// 判断响应实体是否为空
			if (entity != null) {
		//		String responseString = EntityUtils.toString(entity);
		//		System.out.println("response length:" + responseString.length());
		//		System.out.println("response content:" + responseString.replace("\r\n", ""));
			}
			System.out.println(
					"------------------------------------------------------------------------------------------\r\n");
		}
		/**
		 * 把当前cookie从控制台输出出来
		 * 
		 */
		public static void printCookies() {
			System.out.println("headers:");
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
			}
		}
 
		/**
		 * 检查cookie的键值是否包含传参
		 * 
		 * @param key
		 * @return
		 */
		public static boolean checkCookie(String key) {
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			boolean res = false;
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					res = true;
					break;
				}
			}
			return res;
		}
 
		/**
		 * 直接把Response内的Entity内容转换成String
		 * 
		 * @param httpResponse
		 * @return
		 * @throws ParseException
		 * @throws IOException
		 */
		public static String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null)
				return EntityUtils.toString(entity);
			else
				return null;
		}
		
		
		
		public static void main(String[] args) throws ClientProtocolException, IOException {
			
			//用户登陆
			CloseableHttpResponse response = HttpClientKeepSession.post("http://192.168.0.63:12345/driver/driverLogin", "phone=13301330133&code=330133");
			
			printResponse(response);
			printCookies();
			addCookie("name","captureDish","localhost","/BCP/");
			
//			//上传数据
//			HttpClientKeepSession.upload("http://localhost:8080/BCP/all/test/upload");
//			printCookies();
//
		}
		
				
		
		
}
