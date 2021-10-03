package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.selector.Html;
import utils.FileUtil;
import utils.Utilities;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadTest {
    public static void main(String args[]) {
        String url = "http://ip.h5.rd01.sycdn.kuwo.cn/09a933feda7760d422fc74d7af33b15b/612d1ca1/resource/n2/62/26/2263981252.mp3";
//        System.out.println(URLEncoder.encode(url));
//        urlConnection(url);
//        JsoupConnect(url);
    //        postTest(url);
//        getReqTest(url);
        downLoadBinary(url, "半城烟沙.mp3");
//        System.out.println(getContentType(url));
//        System.out.println(Uitilities.getContent(url));
    }


    public static void urlConnection(String url){
        try {
            URLConnection connection = new URL(url).openConnection();
            Utilities.setProperty(connection, "Accept: text/html,application/xhtml+xml,application/xml;" +
                    "q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n");
            connection.connect();
            InputStream is = connection.getInputStream();
            byte bytes[] = new byte[10000];
            while (is.read(bytes) > 0) {
                String content = new String(bytes, "utf-8");
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void JsoupConnect(String url) {
        Connection connection = Jsoup.connect(url);
        connection.ignoreContentType(true);
        Connection.Response response = null;
        try {
            response = connection.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.contentType());
        if (response.contentType().contains("html"))
            System.out.println(new Html(response.body()).getDocument());
    }

    public static void postTest(String url){
        Map<String, Object> form = new HashMap<>();
        Utilities.addForm(form,"plancode: RFP2020123100001\n" +
                "issuecode: \n" +
                "type: ZBGG");
        Request request = new Request(url);
        request.setRequestBody(HttpRequestBody.form(form, "utf-8"));
//        request.setRequestBody(HttpRequestBody.json("{\"siteId\":1,\"pageChannelId", "utf-8");
        request.setMethod("POST");
        Utilities.addHeaderCookie(request, "Origin: http://zb.wut.edu.cn\n" +
                "Referer: http://zb.wut.edu.cn/gongkai_detail_start.html?p=RFP2020123100001&type=ZBGG");
//
        Page page = Utilities.downloadPage(request);
//        System.out.println(page.getHtml());
        System.out.println(page.getJson());
    }

    public static void downLoadBinary(String url, String title) {
        Request request = Utilities.getReq(url);
        request.setBinaryContent(true);
        Page page = Utilities.downloadPage(request);
        byte[] bytes = page.getBytes();
        System.out.println("bytes.length = " + bytes.length);
        FileUtil.writeBytesToFile(bytes, "E:\\music\\" + title);
    }

    public static boolean getReqTest(String url){
        System.out.println(url);
        Request request = new Request(url);
//        Request request = Uitilities.getReq(url);
//        request.setBinaryContent(true);
//        request.setCharset("utf-8");
//        request.setBinaryContent(true);
//        System.out.println("request.toString().length() = " + request.toString().length());
//        String cookie = Uitilities.initialCookie(url);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Utilities.addHeaderCookie(request, "");
        Page page = Utilities.downloadPage(request);
//        writePdf(page);
//        printHeader(page);

        assert page != null;
        System.out.println(page.getHtml().get());
//        System.out.println(page.getJson().get());

        int statusCode = page.getStatusCode();
        System.out.println(statusCode);
        if (statusCode >300) {
            return false;
        }

        try {
            Document document = page.getHtml().getDocument();
            if (document == null || document.text().length() < 300) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(page.getHtml().get());
//        System.out.println(page.getJson());

        return true;
    }

    public static void writePdf(Page page) {
        try {
            byte bytes[] = page.getBytes();
            File file = new File("C:\\Users\\Admin\\Desktop\\txts\\pdf.pdf");
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            dataOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printHeader(Page page) {
        Map<String, List<String>> headers= page.getHeaders();
        for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
            System.out.print(entry.getKey());
            List<String> list = entry.getValue();
            for (String s : list) {
                System.out.print("  \t" + s);
            }
            System.out.println();
        }
    }

    public static String getContentType(String url) {
        if (url == null && !url.toLowerCase().startsWith("http")) {
            return "";
        }
        Page page = Utilities.downloadPage(new Request(url));
        Map<String, List<String>> headers = page.getHeaders();
        if (headers == null) {
            return "";
        }
        List<String> list = headers.get("Content-Type");
        if (list == null) {
            return "";
        }
        return list.get(0);
    }

}
