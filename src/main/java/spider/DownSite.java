package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import utils.CrawUtil;
import utils.FileUtil;
import utils.TextUtiil;
import utils.Utilities;

import javax.swing.*;

public class DownSite extends Thread{
    int ipStart;

    DownSite(int i) {
        ipStart = i;
    }

    @Override
    public void run() {
        for (int i=1; i<255; i++) {
            if (ipStart == 192 && i == 168)
                continue;
            if (ipStart == 172 && i >= 16 && i <= 131)
                continue;

            for (int j=1; j<255; j++) {
                for (int k=1; k<255; k++) {
                    String path = ipStart + "." + i + "." + j + "." + k;
                    downloadPage(path);
                }
            }
        }
    }

    public void downloadPage(String path) {
        String url = "http://" + path;
        try {
            Page page = Utilities.downloadPage(Utilities.getReq(url));
            if (page != null) {
                String content = page.getRawText();
                if (content.length() < 5000 || page.getStatusCode() != 200)
                    return;
                System.out.println("下载 " + url + " 成功");
                content = Utilities.cleanHtml(content);
                String type = CrawUtil.getContentType(page);
                if (type.contains("html")) {
                    Document document = Jsoup.parse(content);
                    completeUrl(document, "a", "href", url);
                    completeUrl(document, "img", "src", url);
                    completeUrl(document, "script", "src", url);
                    completeUrl(document, "link", "href", url);
                    FileUtil.writeStrToFile("E:/AllSites/" + path.substring(0, path.indexOf(".")) + '/' + path.substring(path.indexOf(".") + 1).replace(".", "_") + ".html", document.html());
                    System.out.println("写入 " + url + " 成功");
                } else
                    System.out.println(url + "  " + type);
            }
        } catch (Exception e) {
            System.out.println("download " + url + " 失败");
        }
    }

    public void completeUrl(Element element, String tag, String attr, String Host) {
        Elements elements = element.select(tag);
        for (Element e : elements) {
            String attrCon = e.attr(attr);
            if (attrCon.isEmpty())
                continue;
            e.attr(attr, TextUtiil.removeDots(Host + "/" + attrCon));
        }
    }
}

