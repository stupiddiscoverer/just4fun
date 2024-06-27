package spider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import utils.FileUtil;
import utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class winkawaks {
    static String site = "https://www.winkawaks.org/roms/neogeo/index.htm";
    static String path = "C:\\Users\\张三\\Downloads\\WinKawaks\\roms\\";

    static String[] platforms = {"neogeo", "cps2", "cps1"};

    public static void main(String[] args) {
        for (String s : platforms) {
            List<String> list = new ArrayList<>(300);
            getList(site.replace("neogeo", s), list);
            for (String l : list) {
                downloadZip(site.replace("neogeo/index.htm", s) + "/" +
                        l.replace(".htm", "-download.htm"), s);
            }
        }
    }

    public static void getList(String url, List<String> list) {
        Page page = Utilities.downloadPage(Utilities.getReq(url));
        if (page == null) {
            System.out.println("page is null");
            return;
        }
        Html html = page.getHtml();
        Document document = html.getDocument();
        Elements elements = document.select("#rom-system-index > div > a:nth-child(2)");
        System.out.println(elements.size() + "");
        for (Element e : elements) {
//            System.out.println(e.html());
            list.add(e.attr("href"));
        }
    }

    public static void downloadZip(String url, String platform) {
        Page page = Utilities.downloadPage(Utilities.getReq(url));
        if (page == null) {
            System.out.println("download zip fail");
            return;
        }
        Document document = page.getHtml().getDocument();
        Element element = document.select("#rom-url > div.rom-value > a").first();
        url = element.attr("href");
        String name = element.text().trim();
        page = Utilities.downloadPage(Utilities.getReq(url));
        byte[] bytes = page.getBytes();
        String realPath = path + platform + '\\' + name;
        File file = new File(realPath);
        if (file.exists()) {
            return;
        }
        FileUtil.writeBytesToFile(bytes, path + platform + '\\' + name);
        System.out.println(name + "   " + bytes.length);
    }

}
