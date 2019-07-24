package learntest.httpclient;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import sanri.utils.NumberUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UploadStreamTest {
    @Test
    public void testUploadStream() throws IOException {
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        HttpClient httpClient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();
        HttpPost httpPost = new HttpPost("http://localhost:8081/sanritools/ocr/resolve");

//        ContentBody contentBody =new InputStreamBody(new FileInputStream("d:/logs/controller.log"),ContentType.MULTIPART_FORM_DATA);
        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("image",new FileBody(new File("d:/logs/controller.log"))).build();
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        StatusLine statusLine = response.getStatusLine();
        System.out.println(statusLine);

        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(httpClient);
    }
}
