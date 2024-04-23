package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.nodes.Document;
import spider.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.File;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.FileUtil.readStrFromFile;
import static utils.TextUtiil.getHost;

public class Utilities {

    public static long maxAbsNum (long[] array) {
        if (array == null || array.length < 1)
            return 0;
        long temp = Math.abs(array[0]);
        int len = array.length;
        long abs;
        for (long l : array) {
            abs = Math.abs(l);
            if (temp < abs)
                temp = abs;
        }
        return temp;
    }

    /**
     * 返回1表示正向顺序，返回0代表无顺序， 返回-1代表从大到小
     * */
    public static int isSortArr(int[] arr) {
        if (arr == null || arr.length == 1)
            return 0;
        boolean direction = arr[0] <= arr[arr.length - 1];
        if (direction) {  //由小到大
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] > arr[i + 1])
                    return 0;
            }
            return 1;
        } else {
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] < arr[i + 1])
                    return 0;
            }
            return  -1;
        }

    }

    public static void invertArray(int[] arr) {
        if (arr == null || arr.length == 1)
            return;
        int temp, j;
        int length = arr.length;
        for (int i=0; i<length/2; i++) {
            temp = arr[i];
            j = length - 1 - i;
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static int maxAbsNum (int[] array) {
        if (array == null || array.length < 1)
            return 0;
        int temp = Math.abs(array[0]);
        int len = array.length;
        int abs;
        for (int l : array) {
            abs = Math.abs(l);
            if (temp < abs)
                temp = abs;
        }
        return temp;
    }

    public static long minAbsNum (long[] array) {
        if (array == null || array.length < 1)
            return 0;

        long temp = Math.abs(array[0]);
        int len = array.length;
        long abs;
        for (long l : array) {
            abs = Math.abs(l);
            if (temp > abs)
                temp = abs;
        }
        return temp;
    }



    public static long[] generateRandomArrL(int length, long min, long max) {
        if (min >= max)
            return null;
        long diff = max - min;
        long[] arr = generateRandomArrL(length);
        if (arr == null)
            return null;
        for (int i=0; i<length; i++)
            arr[i] = min + Math.abs(arr[i]%diff);
        return arr;
    }

    public static long[] generateRandomArrL(int length, long max) {
        long[] arr = generateRandomArrL(length);
        if (arr == null || max == 0)
            return null;
        for (int i=0; i<length; i++)
            arr[i] = Math.abs(arr[i]%max);
        return arr;
    }

    public static long[] generateRandomArrL(int length) {
        if (length < 1)
            return null;
        Random random = new Random();
        long[] arr = new long[length];
        for (int i=0; i<length; i++)
            arr[i] = random.nextLong();
        return arr;
    }

    public static int[] generateRandomArr(int length, int min, int max) {
        if (min >= max)
            return null;
        int diff = max - min;
        int[] arr = generateRandomArr(length);
        if (arr == null)
            return null;
        for (int i=0; i<length; i++)
            arr[i] = min + Math.abs(arr[i]%diff);
        return arr;
    }

    public static int[] generateRandomArr(int length, int max) {
        int[] arr = generateRandomArr(length);
        if (arr == null || max == 0)
            return null;
        for (int i=0; i<length; i++)
            arr[i] = Math.abs(arr[i]%max);
        return arr;
    }

    public static int[] generateRandomArr(int length) {
        if (length < 1)
            return null;
        Random random = new Random();
        int[] arr = new int[length];
        for (int i=0; i<length; i++)
            arr[i] = random.nextInt();
        return arr;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty())
            return true;
        return false;
    }

    public static Document downloadReq(Request request) {
        Page page = downloadPage(request);
        Html html = page.getHtml();
        if(html == null)
            return null;
        String text = html.toString();
        text = text.replace("\\&quot;", "").replace("&nbsp;", " ").
                replace("\\/", "/").replace("\\\"", "\"");
        return new Html(text).getDocument();
    }

    public static Page downloadPage(Request request) {
        Downloader downloader = new HttpClientDownloader();
        try {
            Page page = downloader.download(request, new Task() {
                @Override
                public String getUUID() {
                    return request.getUrl();
                }

                @Override
                public Site getSite() {
                    Site site = new Site();
                    site.setTimeOut(60000);
//                    site.setDisableCookieManagement(true);
                    return site;
                }
            });
            return page;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从浏览器复制多条头信息加到request中
     * @param head 复制的字符串
     * @param request 要加的request
     */
    public static void addHeaderCookie(Request request, String head){
        String lines[] = head.split("\n");
        for (String s : lines){
            s = s.trim();
            String pair[] = s.split(": ");
            if (pair.length != 2) {
                return;
            }
            if (pair[0].toLowerCase().equals("cookie")) {
                for (String cook : pair[1].split(";")) {
                    String cooki[] = cook.split("=");
                    if (cooki.length == 1) {
                        request.addCookie(cooki[0].trim(), "");
                    }
                    if (cooki.length == 2) {
                        request.addCookie(cooki[0].trim(), cooki[1].trim());
                    }
                }
                continue;
            }
            request.addHeader(pair[0], pair[1]);
        }
    }

    public static void addForm(Map form, String str) {
        String attributes[] = str.split("\n");
        for (String s : attributes) {
            String keyValue[] = s.split(": ");
            if (keyValue.length == 2)
                form.put(keyValue[0], keyValue[1]);
            else
                form.put(keyValue[0], "");
        }
    }

    public static int getNum(int rank, String str) {
        int[] nums = getNums(str);
        if (nums == null) {
            return 0;
        }
        int length = nums.length;
        if (rank >= length || rank < -length) {
            return 0;
        }
        if (rank < 0) {
            rank = length + rank;
        }
        return nums[rank];
    }

    /**
     * 获取字符串中所有数字，按顺序排放
     * */
    public static int[] getNums(String str) {
        if (str == null) {
            return null;
        }
        LinkedList<Integer> nums = new LinkedList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        while (matcher.find()) {
            if (matcher.group().length() < 10) {
                nums.add(Integer.parseInt(matcher.group()));
            }
        }
        int size = nums.size();
        int[] arr = new int[size];
        for(int i=0; i<size; i++) {
            arr[i] = nums.get(i);
        }
        return arr;
    }

    /**
     * 格式示范path = "content > list[0] > data[3] > publish"
     * */
    public static JSONObject getJsonObject(String path, JSONObject jsonObject) {
        if (path == null || jsonObject == null) {
            return null;
        }

        JSONObject temp = new JSONObject(jsonObject);
        String[] parts = path.split(">");
        for (String s : parts) {
            if (s.trim().isEmpty()) {
                continue;
            }

            if (s.contains("[") && s.contains("]")) {
                JSONArray jsonArray = temp.getJSONArray(s.split("\\[")[0].trim());
                if (jsonArray == null || jsonArray.isEmpty()) {
                    return null;
                }
                temp = jsonArray.getJSONObject(getNum(-1, s));
            } else {
                temp = temp.getJSONObject(s.trim());
            }

            if (temp == null) {
                return null;
            }
        }
        return temp;
    }

    public static String getJsonString(String path, JSONObject jsonObject) {
        if (path == null) {
            return null;
        }
        String parts[] = path.split(">");
        String key = parts[parts.length - 1].trim();
        if (key.trim().isEmpty()) {
            return null;
        }

        path = "";
        for (int i=0; i<parts.length-1; i++) {
            path += parts[i] + ">";
        }

        JSONObject temp = new JSONObject(jsonObject);
        temp = getJsonObject(path, temp);

        if (temp == null) {
            return null;
        }
        return temp.getString(key);
    }

    public static int getJsonInt(String path, JSONObject jsonObject) {
        JSONObject temp = new JSONObject(jsonObject);
        return getNum(-1, getJsonString(path, temp));
    }

    public static JSONArray getJsonArray(String path, JSONObject jsonObject) {
        if (path == null) {
            return null;
        }
        String parts[] = path.split(">");
        String key = parts[parts.length - 1].trim();
        if (key.trim().isEmpty()) {
            return null;
        }

        path = "";
        for (int i=0; i<parts.length-1; i++) {
            path += parts[i] + ">";
        }

        JSONObject temp = new JSONObject(jsonObject);
        temp = getJsonObject(path, temp);

        if (temp == null) {
            return null;
        }

        JSONArray jsonArray;
        if (key.contains("[") && key.contains("]")) {
            jsonArray = temp.getJSONArray(key.split("\\[")[0].trim());
            if (jsonArray == null || jsonArray.isEmpty()) {
                return jsonArray;
            }
            jsonArray = jsonArray.getJSONArray(getNum(-1, key));
        } else {
            jsonArray = temp.getJSONArray(key);
        }
        return jsonArray;
    }

    /**
     * 从page.getHeader()获取Set-Cookie
     * 适合网站直接返回cookie信息的
     * 调用RequestUtil.addHeaderCookie()请在String头部加"Cookie: "或"cookie: "视情况而定
     */
    public static String setCookie(Page page) {
        Map<String, List<String>> headers = page.getHeaders();
        if (headers == null) {
            return "";
        }
        String[] keys = {"Set-Cookie", "set-cookie", "cookie", "Cookie"};
        List<String> cook;
        StringBuilder cookie = new StringBuilder();

        for (String k : keys) {
            cook = headers.get(k);
            if (cook != null && !cook.isEmpty()) {
                for (String c : cook) {
                    cookie.append(c.split(";")[0].trim()).append("; ");
                }
                break;
            }
        }

        return cookie.toString();
    }

    /**
     * 针对security_session_mid_verify 和 security_session_verify验证
     * @param url 列表页完整url
     * 返回的cookie添加到request:    RequestUtil.addHeaderCookie(request, "Cookie: " + cookie);
     * */
    public static String initialCookie(String url) {
        Request request = getReq(url);
        Page page = downloadPage(request);
        String cookie = setCookie(page);        //获取security_session_verify

        request = yunSuoAutoJump(url);
        addHeaderCookie(request, "Cookie: " + cookie);
        page = downloadPage(request);
        cookie += setCookie(page);              //获取security_session_mid_verify

        return cookie;
    }

    public static void setProperty(URLConnection connection, String properties){
        String lines[] = properties.split("\n");
        for (String s : lines) {
            String pair[] = s.split(": ");
            if (pair.length != 2)
                return;
            if (pair[0].equals("Cookie")) {
                for (String cook : pair[1].split("; ")) {
                    String cooki[] = cook.split("=");
                    if (cooki.length != 2)
                        continue;
                    connection.setRequestProperty(cooki[0], cooki[1]);
                }
                continue;
            }
            connection.setRequestProperty(pair[0], pair[1]);
        }
    }

    /**
     * 组合一个常用的 GET 请求
     */
    public static Request getReq(String url) {
        Request request = new Request(url);
        request.setMethod(HttpConstant.Method.GET);
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        return request;
    }

    /**
     * initialCookie()专用
     * */
    private static Request yunSuoAutoJump(String url) {
        String screenData = "1920,1080";
        String cookie = "srcurl=" + stringToHex(url) + ";path=/;";
        url = getHost(url) + "/index.php?security_verify_data=" + stringToHex(screenData);
        Request request = new Request(url);
        addHeaderCookie(request, "Cookie: " + cookie);
        return request;
    }

    public static String stringToHex(String str) {
        StringBuilder hex = new StringBuilder();
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            hex.append(Integer.toHexString(c));
        }
        return hex.toString();
    }

    public static String[] getCityPinYin() {
        String[] keyValues;
        String string = readStrFromFile("C:\\Users\\Admin\\Desktop\\txts\\cityName.txt");
        string = string.replace("{", "").replace("}", "")
                .replace("\"", "").replace(" ", "").replace("\n", "");
        String[] parts = string.split(",");
        keyValues = new String[parts.length];
        String [] partsSmall;
        for (int i=0; i<parts.length; i++) {
            partsSmall = parts[i].split(":");
            keyValues[i] = partsSmall[0].trim() + ",," + partsSmall[1].trim();
        }
        return keyValues;
    }

    public static Map<String, String> getProvincePinYin() {
        Map<String, String> keyValues = new HashMap<>();
        String string = readStrFromFile("C:\\Users\\Admin\\Desktop\\txts\\provincePinYin.txt");
        String[] parts = string.split("\n");
        String [] partsSmall;
        for (int i=0; i<parts.length; i++) {
            partsSmall = parts[i].split("    ");
            keyValues.put(partsSmall[1].trim(), partsSmall[0].trim());
        }
        return keyValues;
    }

    public static void buildVideoPath(byte[] bytes, String fileName) {
        fileName = fileName.trim().replace("\t", "")
                .replace("\r", "").replace("\n", "")
                .replace(" ", "_");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long time = new Date().getTime();
        String now = dateFormat.format(time);
        String today = now.split(" ")[0];
        String hour = now.split(" ")[1].split(":")[0];
        System.out.println(now);
        String dir = "E:\\video\\" + today;
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        dir += "\\" + hour;
        directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        int len = bytes.length;
        String prefix = len + "B";
        if (bytes.length > 1000) {
            prefix = len/1000 + "KB";
        }
        if (bytes.length > 1000*1000) {
            prefix = len/1000/1000 + "MB";
        }
        fileName = dir + "\\" + prefix + "-" + fileName + ".mp4";
        FileUtil.writeBytesToFile(bytes, fileName);
    }

    public static void getAllFiles(File file, LinkedList<File> files) {
        if (file == null || !file.exists() || files == null)
            return;
        if (file.isFile()) {
            files.add(file);
            return ;
        }
        File[] files1 = file.listFiles();
        for (File f : files1)
            getAllFiles(f, files);
    }

    public static String cleanHtml(String content) {
        return content.replace("&ensp;", " ").replace("&emsp;", " ")
                .replace("&nbsp;", " ").replace("&lt;", "<")
                .replace("&gt;", ">").replace("&amp;", "&")
                .replace("&quot;", "\"").replace("&copy;", "©")
                .replace("&reg;", "®").replace("&times;", "×")
                .replace("&divide;", "÷").replace("&yen;", "¥");
    }

}
