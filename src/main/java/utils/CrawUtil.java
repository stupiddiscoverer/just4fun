package utils;

import us.codecraft.webmagic.Page;

import java.util.List;
import java.util.Map;

public class CrawUtil {

    public static final String[] textTypes = {
            "html", "text", "javascript", "json", "rss", "atom",
    };
    public static final String[] byteTypes = {
            "video", "audio", "image", "xop", "stream", "application",
    };

    public static boolean typeLikeByte(Page page) {
        String contentType = getContentType(page);
        if (contentType.isEmpty() || TextUtiil.isContain(textTypes, contentType)) {
            return false;
        }
        return TextUtiil.isContain(byteTypes, contentType);
    }

    public static String getContentType(Page page) {
        try {
            Map<String, List<String>> headers = page.getHeaders();
            for (Map.Entry<String, List<String>> entry: headers.entrySet()) {
                if (entry.getKey().equalsIgnoreCase("content-type")) {
                    List<String> list = entry.getValue();
                    String type = list.get(0);
                    return type;
                }
            }
        } catch (Exception e) {
            System.out.println("getContentType()失败");
        }
        return "";
    }

}
