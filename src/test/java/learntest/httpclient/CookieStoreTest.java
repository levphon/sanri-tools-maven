package learntest.httpclient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import sanri.utils.NumberUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class CookieStoreTest {

    @Test
    public void testCookieStore() throws IOException {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        HttpClient  httpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();
        HttpPost httpPost = new HttpPost("http://192.168.0.63:12345/driver/driverLogin");

        NameValuePair nameValuePair = new BasicNameValuePair("phone", "17620411498");
        NameValuePair code = new BasicNameValuePair("code", "942634");

        HttpEntity httpEntity = new UrlEncodedFormEntity(Arrays.asList(nameValuePair, code));
        httpPost.setEntity(httpEntity);

        System.out.println("headers:");
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost);
        Header[] allHeaders = response.getAllHeaders();
        for (Header allHeader : allHeaders) {
            System.out.println(allHeader);
        }

        System.out.println("cookies:");
        List<Cookie> cookies = basicCookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println(cookie);
            String name = cookie.getName();
            if("SESSION".equals(name)){
                String value = cookie.getValue();
                // 保存此值 TODO
                System.out.println(value);
            }
        }

        System.out.println("entity");
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
}
