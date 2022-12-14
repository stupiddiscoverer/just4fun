package tools;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;

public class ParseSelector {
    public static String parse(Document document) {
        Elements elements = document.getAllElements();
        for (Element e : elements) {
            String text = e.text();
            if (text.isEmpty() || !e.children().isEmpty())
                continue;
            System.out.println(e.cssSelector() + "\t\t" + cleanText(text));
        }
        return "shit";
    }

    public static String cleanText(String text) {
        return text.replace("\r", "").replace("\n", "")
                .replace("\t", "");
    }
}
