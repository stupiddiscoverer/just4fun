package spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import utils.FileUtil;
import utils.TextUtiil;
import utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class CloudTopS8 {
    public static ArrayList<String> allHeroes = new ArrayList<>(64);
    public static ArrayList<Integer> heroPrices = new ArrayList<>(64);
    public static ArrayList<String> allRestrains = new ArrayList<>(32);
    public static ArrayList<LinkedList<Integer>> restrainNum = new ArrayList<>(32);

    static int[][] heroRestrainMap = new int[64][32];

    public static void print2DArray(int[][] arr) {
        for (int j=0; j<arr[0].length; j++) {
            for (int i=0; i<arr.length; i++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        getAllHeroes();
        File file = new File(".");
        TextUtiil.printArray(file.list());
        String html = FileUtil.readStrFromFile("./src/main/java/spider/s8.txt");
        Document document = Jsoup.parse(html);
        Elements elements = document.select(".overview-list > li");
        System.out.println(elements.html());
    }

    private static void parseHeroAndRestrainFromWeb(String url, int price) {
        String html = downloadHtml(url);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div.linfo > div.contentbox > p");
        Elements spans = elements.select("span");
        spans.remove();
        html = elements.html();
        html = html.substring(html.indexOf("<strong>"));
//        System.out.println(html);
        String[] lines = html.split("\n");
        String line;
        for (int i=0; i<lines.length; i++) {
            line = lines[i];
            if (line.startsWith("<strong>")) {
                String name = line.substring(line.indexOf('>') + 1, line.indexOf('<', 5));
                if (name.isEmpty()) {
                    continue;
                }
                if (allRestrains.contains(name)) {
                    System.out.println("重复英雄名。。。" + name);
                    return;
                }
                allHeroes.add(name);
                heroPrices.add(price);
//                System.out.println(name);
            }
            if (line.startsWith("羁绊：")) {
                String[] restrains = line.substring(line.indexOf("：") + 1).split("、");
                for (int j=0; j<restrains.length; j++) {
                    restrains[j] = restrains[j].trim();
                    if (restrains[j].contains("法力值")) {
                        restrains[j] = restrains[j].substring(0, restrains[j].indexOf("法力值")).trim();
                    }
                    if (!allRestrains.contains(restrains[j])) {
//                        System.out.println("羁绊：  " + restrains[j]);
                        allRestrains.add(restrains[j]);
                        heroRestrainMap[allHeroes.size() - 1][allRestrains.size() - 1] = 1;
                    } else {
                        heroRestrainMap[allHeroes.size() - 1][allRestrains.indexOf(restrains[j])] = 1;
                    }
                }
            }
        }
    }

    private static void getAllHeroes() {
        String url = "https://m.ali213.net/news/gl2211/953951.html";
        parseHeroAndRestrainFromWeb(url, 1);
        for (int i=2; i<=5; i++) {
            parseHeroAndRestrainFromWeb(url.replace(".html", "_" + i + ".html"), i);
        }
        System.out.println(allHeroes.size());
        System.out.println(allRestrains.size());
        TextUtiil.printArray(allHeroes);
        TextUtiil.printArray(allRestrains);
//        FileUtil.writeArrayToFile();
//        print2DArray(heroRestrainMap);
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
}
