package spider;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import utils.CrawUtil;
import utils.FileUtil;
import utils.TextUtiil;
import utils.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class WebsiteDownload {
    private static final Map<String, String> tagAndSrc = new HashMap<>();
    static {
        tagAndSrc.put("img", "src");
        tagAndSrc.put("a", "href");
        tagAndSrc.put("iframe", "src");
        tagAndSrc.put("script", "src");
        tagAndSrc.put("link", "href");
    }

    private static final String baseDir = "C:/Users/fly/Desktop/html/";
    private static final Set<String> linkRecord = new HashSet<>();
    private static String URL = "https://demo.wpyou.com/?wptheme=WPUniversity";
    private static String HOST = TextUtiil.getHost(URL) + "/";
    private static ArrayList<urlHtml> pages = new ArrayList<>();

    @Data
    private static class urlHtml {
        String url;
        String html;
        public urlHtml(String a, String b) {
            url = a;
            html = b;
        }
    }

    public static void main(String[] args) {
        craw(URL);
        for (String s : linkRecord) {
            System.out.println(s);
        }
    }

    public static void craw(String url) {
        String html = downloadHtml(url);
        url = urlToDir(url);
        pages.add(new urlHtml(url, html));
        for (int i=0; i<pages.size(); i++) {
            html = pages.get(i).html;
            if (html == null || html.isEmpty()) {
                continue;
            }

            Document document = Jsoup.parse(html);
            for (Element element : document.getAllElements()) {
                String tagName = element.tagName();
                if (!tagAndSrc.keySet().contains(tagName)) {
                    continue;
                }

                String link = element.attr(tagAndSrc.get(tagName));
                if (!link.startsWith(HOST) || link.length() <= HOST.length() + 3 || linkRecord.contains(link)) {
                    continue;
                }
                linkRecord.add(link);

                Page page = download(link);
                link = urlToDir(link);
                if (CrawUtil.getContentType(page).startsWith("text/html")) {
                    if (!link.substring(link.length() - 10).contains(".")) {
                        link = link + ".html";
                    }
                    pages.add(new urlHtml(link, new String(page.getBytes())));
                } else {
                    if (CrawUtil.getContentType(page).startsWith("application/json")) {
                        if (!link.substring(link.length() - 10).contains(".")) {
                            link = link + ".json";
                        }
                    }
                    saveBytes(page, link);
                }
                element.attr(tagAndSrc.get(tagName), link);
            }
            saveHtml(document.html(), pages.get(i).url);
        }
    }

    private static String urlToDir(String link) {
        link = link.replace(HOST, baseDir)
                .replace("?", "_");
        while (link.endsWith("/")) {
            link = link.substring(0, link.length() - 1);
        }
        try {
            link = URLDecoder.decode(link, "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] http = {
                "http:", "https:",
        };
        for (String s : http) {
            int index = link.indexOf(s, 10);
            if (index > 0) {
                link = link.substring(index);
            }
        }
        return changeHost(link);
    }

    private static String changeHost(String url) {
        String host = TextUtiil.getHost(url);
        if (baseDir.startsWith(host)) {
            return url;
        }
        url = baseDir.substring(0, baseDir.length() - 1) + url.substring(host.length());
        return url;
    }

    private static Page download(String link) {
        Request request = Utilities.getReq(link);
        request.setBinaryContent(true);
        return Utilities.downloadPage(request);
    }

    private static String downloadHtml(String url) {
        Request request = Utilities.getReq(url);
        Page page = Utilities.downloadPage(request);
        String html = page.getRawText();
        if (html == null || html.isEmpty()) {
            return null;
        }
        return html;
    }

    private static void saveHtml(String html, String url) {
        html = html.replace(HOST, "");
        FileUtil.writeStrToFile(url, html);
    }

    private static void saveBytes(Page page, String url) {
        FileUtil.writeBytesToFile(page.getBytes(), url);
    }
}
