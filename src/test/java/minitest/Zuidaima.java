package minitest;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Zuidaima {
    public static void main(String[] args) throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie basicClientCookie = new BasicClientCookie("zdmid", "CA657ADB4342BCBE0E31434F3579D6BD");
        basicClientCookie.setDomain(".zuidaima.com");
        cookieStore.addCookie(basicClientCookie);
        cookieStore.addCookie(new BasicClientCookie("zuidaima_id","-RG6htqdug84BgpC"));
        cookieStore.addCookie(new BasicClientCookie("Hm_lvt_500f123d596f6dae47e36a9a36fed240","1557974988,1557978037"));
        cookieStore.addCookie(new BasicClientCookie("Hm_lpvt_500f123d596f6dae47e36a9a36fed240","1557991757"));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        ContentType contentType = ContentType.MULTIPART_FORM_DATA.withCharset("utf-8");
        HttpPost httpPost = new HttpPost("http://www.zuidaima.com/mood/create.htm");
        HttpEntity httpEntity = MultipartEntityBuilder.create().addTextBody("rdm", "gBFEe",contentType)
                .addTextBody("content", "下午好,还有3 小时下班",contentType).build();
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        System.out.println(statusCode);

        if(statusCode == 302){
             Header firstHeader = response.getFirstHeader("location");
            String location = firstHeader.getValue();
            System.out.println("重定向到:"+location);
        }

        response.close();
        httpClient.close();
    }

    /**
     * 爬取所有的心情,保存进文件
     */
    @Test
    public void testMood() throws IOException {
//        Document document = Jsoup.connect("http://www.zuidaima.com/mood.htm?p=1").timeout(60000).get();
//        //找到最后一页
//        Element $pageLastLi = document.select(".pagination>li").last();
//        Element $prev = $pageLastLi.previousElementSibling();
//        int pageCount = NumberUtil.toInt($prev.text());

        //第一页的不要了,直接取 100 页的心情
        FileOutputStream fileOutputStream = FileUtils.openOutputStream(new File("d:/test/mood.txt"), true);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
        for (int i = 2; i < 100 ; i++) {
            Document pageDoc = Jsoup.connect("http://www.zuidaima.com/mood.htm?p="+i).timeout(60000).get();
            Elements $datas = pageDoc.select(".data");
            for (int j = 0; j < $datas.size(); j++) {
                Element $data = $datas.get(j);
                String text = $data.text();
                bufferedWriter.write(text+"\r\n");
            }
            bufferedWriter.flush();

        }

        bufferedWriter.close();
    }
}
