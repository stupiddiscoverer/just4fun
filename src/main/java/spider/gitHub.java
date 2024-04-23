package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import utils.FileUtil;
import utils.TextUtiil;
import utils.Utilities;


public class gitHub {
    static String site = "https://github.com/ddz16/TSFpaper?tab=readme-ov-file";

    public static void main(String[] args) {
        String html = FileUtil.readStrFromFile("./src/main/java/spider/copy.html");
        Document document = Jsoup.parse(html);
        if (document == null) {
            return;
        }
        parseUrls(document);
    }

    static void parseUrls(Document document)
    {
        Elements titles = document.select("article > div > h2");
        System.out.println(titles.size());
        Elements tables = document.select("article > table ");
        System.out.println(tables.size());
        for (int i=1; i<titles.size(); i++)
        {
            downloadTable(titles.get(i).text(), tables.get(i-1));
        }
    }

    static void downloadTable(String title, Element table)
    {
        title = title.replace(" ", "_");
        String rootDir = "C:/Users/fly/Desktop/人工智能论文/";
        System.out.println(title);
        Elements tableTitles = table.select("thead > tr > th");
        Elements contents = table.select("tbody > tr");
        int yearIndex = 0;
        int urlIndex = 0;
        int titleIndex = 0;
        int i = 0;
        for (Element element : tableTitles)
        {
            if (element.text().equals("Conference"))
            {
                yearIndex = i;
            }
            if (element.text().equals("Method"))
            {
                urlIndex = i;
            }
            if (element.text().contains("Title"))
            {
                titleIndex = i;
            }
            i++;
        }
        System.out.println(yearIndex);
//        System.out.println(urlIndex);
        for (Element content : contents)
        {
            Elements tds = content.select("td");
            int year = Utilities.getNum(-1, tds.get(yearIndex).text());
            if (year >= 2021)
            {
                System.out.println(year);
                Page page = downloadUrl(TextUtiil.parseHref(tds.get(urlIndex).html()));
                String pageTitle = tds.get(titleIndex).text().replace(" ", "_")
                        .replace(":", "_");
                if (page != null)
                {
                    byte[] bytes = page.getBytes();
                    if (bytes == null)
                    {
                        continue;
                    }
                    if(bytes.length < 100*1000)
                    {
                        System.out.println(page.getHtml());
                        continue;
                    }
                    FileUtil.writeBytesToFile(bytes, rootDir + title + "/" + pageTitle + ".pdf");
                }
            }
        }
    }

    static Page downloadUrl(String url)
    {
        System.out.println(url);
        if (url.contains("arxiv"))
        {
            return downloadArxiv(url);
        }
        if (url.contains("openreview.net"))
        {
            return downloadOpenreview(url);
        }
//        System.out.println(url);
        return null;
    }

    static Page downloadSeqml(String url)
    {
        Page page = Utilities.downloadPage(Utilities.getReq(url));
        assert page != null;
        Document document = page.getHtml().getDocument();
        Elements a = document.select("a");
        for(Element element : a){
            String href = TextUtiil.parseHref(a.html());
            if (href.contains("openreview.net"))
            {
                return downloadOpenreview(href);
            }
        }
        return null;
    }

    static Page downloadOpenreview(String url)
    {
        Page page = Utilities.downloadPage(Utilities.getReq(url.replace("forum", "pdf")));
        assert page != null;
        return page;
    }

    static Page downloadArxiv(String url)
    {
        Page page = Utilities.downloadPage(Utilities.getReq(url.replace("abs", "pdf") + ".pdf"));
        assert page != null;
        return page;
    }

}
