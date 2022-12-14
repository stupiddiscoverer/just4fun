package spider;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import utils.FileUtil;
import utils.TextUtiil;
import utils.Utilities;


public class VideoSpider {
    private int maxPage = 1350;
    private static String HOST = "http://www.xiaobi060.com/";

    public void download(int start, int end) {
        for (int i=start; i<=maxPage && i<end; i++) {
            Request request = generatePageLinks(i);
            FileUtil.log("downloading pageNo: " + i);
            System.out.println(request.getUrl() + "\n\n");
            parseVideoLinkInPage(request);
        }
    }

    private void setMaxPage(Document document) {
        try {
            Element a = document.select(".last > a").first();
            if (a == null) {
                System.out.println("未找到最大页数");
                return;
            }
            int max = Utilities.getNum(-1, a.attr("href"));
            if (max > maxPage) {
                maxPage = max;
            }
            FileUtil.log("maxPage = " + maxPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Request generatePageLinks(int pageNo) {
        if (pageNo == 1) {
            return new Request(HOST);
        }

        String url = "http://www.xiaobi060.com/latest-updates/%d/";
        Request request = new Request(String.format(url, pageNo));
        return request;
    }

    private void parseVideoLinkInPage(Request request) {
        Page page = Utilities.downloadPage(request);
        String html = page.getRawText();
        if (StringUtils.isEmpty(html)) {
            return;
        }
        Document document = Jsoup.parse(html);
        if (document == null) {
            return;
        }
        setMaxPage(document);
        Elements elements = document.select(".item");
        System.out.println("共有" + elements.size() + "个视频");

        for (Element e : elements) {
            try {
                String title = e.select(".title").text().trim();
                Elements aTags = e.select("a");
                if (aTags.isEmpty()) {
                    continue;
                }

                Element aTag = aTags.first();
                String href = aTag.attr("href");
                String url = TextUtiil.removeDots(HOST + href);
                System.out.println("downloading videoPage: " + url);
                page = Utilities.downloadPage(new Request(url));
                if (page == null) {
                    continue;
                }
                html = page.getRawText();
                if (StringUtils.isEmpty(html)) {
                    continue;
                }
                document = Jsoup.parse(html);
                if (document == null) {
                    continue;
                }
                aTags = document.select(".info > .item > a");
                if (aTags.isEmpty()) {
                    continue;
                }
                aTag = aTags.last();
                String text = aTag.text().trim();
                int size = Utilities.getNum(1, text);
                if (size > 30) {
                    System.out.println("文件超过30M， 跳过");
                    continue;
                }

                href = aTag.attr("href");
                url = TextUtiil.removeDots(HOST + href);
                downloadVideo(url, title);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void downloadVideo(String url, String title) {
        try {
            System.out.println("downloading video: " + title);
            int index = url.indexOf("/?download=");
            if (index > 0)
                url = url.substring(0, index);
            System.out.println(url + "\n");
            Request request = new Request(url);
            request.setBinaryContent(true);
            Page page = Utilities.downloadPage(request);
            if (page == null) {
                System.out.println("下载失败");
                return;
            }
            byte[] bytes = page.getBytes();
            if (bytes.length < 1000*1000) {
                System.out.println("下载失败！！！");
                return;
            }
            System.out.println("bytes.length = " + bytes.length);
            Utilities.buildVideoPath(bytes, title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}