package utils;

import org.apache.commons.codec.binary.Base64;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static utils.FileUtil.readStrFromFile;
import static utils.FileUtil.writeStrToFile;
import static utils.Utilities.maxAbsNum;

public class TextUtiil {
    public static String getHost(String url) {
        if (url == null) {
            return null;
        }
        String[] parts = url.split("/");
        if (parts.length < 3) {
            return url;
        }
        return parts[0] + "//" + parts[2];
    }

    public static String getCurrentDir(String url){
        if(url == null)
            return null;
        int index = url.lastIndexOf("/");
        if (index < 0)
            return url;
        return url.substring(0, index + 1);
    }

    public static boolean base64StrToBi(String path){
        if (path == null || path == "") {
            path = "C:\\Users\\Admin\\Desktop\\txts\\pdf.pdf";
        }
        try{
            String content = readStrFromFile(path, "utf-8");
            byte[] bytes = Base64.decodeBase64(content);
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(bytes);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void formattingJs(String path) {
        if (path == null || path == "") {
            path = "C:\\Users\\Admin\\Desktop\\txts\\temp.js";
        }
        String js = readStrFromFile(path, "utf-8");
        js = js.replace("}", "}\n").replace("var ", "\nvar ")
                .replace("{", "\n{");
        System.out.println(js);
        writeStrToFile(js, path);
    }

    //unicode编码转中文
    public static String decodeUnicode(String dataStr) {
        int start = 0;
        int end;
        String preFix[] = {"\\u", "%u"};
        int index = 0;
        final StringBuilder builder = new StringBuilder();
        while (start < dataStr.length()) {
            end = dataStr.indexOf(preFix[index], start);
            if(end == -1) {
                index = 1;
                end = dataStr.indexOf(preFix[index], start);
            }
            if(end == -1){
                builder.append(dataStr.substring(start));
                break;
            }
            if(end + 6 > dataStr.length()) {
                builder.append(dataStr.substring(end));
                break;
            }
            builder.append(dataStr, start, end);
            String charStr = dataStr.substring(end + 2, end + 6);
            try {
                char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串
                builder.append(new Character(letter).toString());
            }catch (Exception e){
                builder.append(preFix[index] + charStr);
            }
            start = end + 6;
        }
        return builder.toString();
    }

    //汉字转换成二进制字符串
    public static String strToBinStr(String str) {
        char[] chars=str.toCharArray();
        StringBuffer result = new StringBuffer();
        for(int i=0; i<chars.length; i++) {
            result.append(Integer.toBinaryString(chars[i]));
            result.append(" ");
        }
        return result.toString();
    }
    //二进制字符串转换成汉字
    public static String BinStrTostr(String binary) {
        String[] tempStr = binary.split(" ");
        char[] tempChar=new char[tempStr.length];
        for(int i=0;i<tempStr.length;i++) {
            tempChar[i]=BinstrToChar(tempStr[i]);
        }
        return String.valueOf(tempChar);
    }
    //将二进制字符串转换成int数组
    public static int[] BinstrToIntArray(String binStr) {
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
    //将二进制转换成字符
    public static char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }
        return (char)sum;
    }
    //中文转unicode编码
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        StringBuilder unicodeBytes = new StringBuilder();
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2)
                unicodeBytes.append(utfBytes[i]);
            else
                unicodeBytes.append("\\u" + hexB);
        }
        return unicodeBytes.toString();
    }

    public static String rmHtmlAnnotation(String html){
        if(html == null)
            return null;
        int start = html.indexOf("<!--");
        int end = html.indexOf("-->", start);
        while(start != -1 && end != -1){
            html = html.replace(html.substring(start, end + 3),"");
            start = html.indexOf("<!--");
            end = html.indexOf("-->", start);
        }
        return html;
    }

    /**
     * 去除url中的”/abc/../“这种多余的字符串，不会影响host
     * http://abc.com/../abc/ac.html只会去除”../"
     * 替换"/./"和"//"为"/"
     * */
    public static String removeDots(String url) {
        if (url == null || !url.contains(":")) {
            return url;
        }
        if (url.length() > 5 && url.substring(5).contains("http")) {
            url = url.substring(5);
            url = url.substring(url.indexOf("http"));
        }

        url = url.replace("/./", "/");
        while (url.contains("//")) {
            url = url.replace("//", "/");
        }
        url = url.replace(":/", "://");

        int hostIndex = 3;
        String parts[] = url.split("/");
        if (!parts[0].toLowerCase().startsWith("http")) {
            if (parts[0].contains(".")) {
                hostIndex = 1;
            } else {
                hostIndex = 0;
            }
        }

        for (int i=hostIndex; i<parts.length; i++) {
            int j = i-1;
            if (parts[i].equals("..") && j >= hostIndex) {
                parts[i] = "";
                while (parts[j].isEmpty() && j >= hostIndex) {
                    j--;
                }
                parts[j] = "";
            }
        }

        String simpleUrl = "";
        for (int i=0; i<hostIndex; i++) {
            simpleUrl += parts[i] + "/";
        }

        for (int i=hostIndex; i<parts.length; i++) {
            if (parts[i].isEmpty() || parts.equals("..")) {
                continue;
            }
            simpleUrl += parts[i] + "/";
        }

        if (url.endsWith("/")) {
            return simpleUrl;
        }
        return simpleUrl.substring(0, simpleUrl.length() - 1);
    }

    public static String getJSRandom() {
        String rand16 = "0.";
        Random rand = new Random();
        for (int j=0; j<2; j++) {
            int len8 = rand.nextInt() % 100000000;
            String rand8 = String.valueOf(len8);
            if (rand8.startsWith("-")) {
                rand8 = rand8.substring(1);
            }
            int len = rand8.length();
            for (int i=0; i<8-len; i++) {
                rand8 = "0" + rand8;
            }
            rand16 += rand8;
        }
        return rand16;
    }

    public static void printArray(String[] strings) {
        for (String s : strings)
            System.out.println(s);
    }

    public static void printArray (int[] array) {
        int len = array.length;
        //十行十列
        int jump = len / 100;
        jump = Math.max(1, jump);

        int maxLen = deciNumLen(maxAbsNum(array));

        for (int i=0; i<len; i+=jump) {
            if (i % (10*jump) == 0)
                System.out.println();
            System.out.print(array[i]);
            for (int j=deciNumLen(array[i]); j<maxLen; j++) {
                System.out.print(" ");
            }
            System.out.print("\t");
        }
        System.out.println();
    }

    public static int deciNumLen (long l) {
        int len = 1;
        if (l < 0){
            len++;
            l = -l;
        }
        while ((l = l/10) > 0)
            len++;
        return len;
    }

    public static int deciNumLen (int l) {
        int len = 1;
        if (l < 0){
            len++;
            l = -l;
        }
        while ((l = l/10) > 0)
            len++;
        return len;
    }

    public static void printArray (long[] array) {
        int len = array.length;
        //十行十列
        int jump = len / 100;
        jump = Math.max(1, jump);

        int maxLen = deciNumLen(maxAbsNum(array));

        for (int i=0; i<len; i+=jump) {
            if (i % (10*jump) == 0)
                System.out.println();
            System.out.print(array[i]);
            for (int j=deciNumLen(array[i]); j<maxLen; j++) {
                System.out.print(" ");
            }
            System.out.print("\t");
        }
        System.out.println();
    }

    public static String removeSimpleFrontEnd(String[] urls) {
        if (urls == null)
            return "";
        for (String s : urls)
            if (s == null || s.isEmpty())
                return "";

        int frontIndex = 0, endIndex = -1;
        char frontC = urls[0].charAt(frontIndex);
        char endC = urls[0].charAt(urls[0].length() + endIndex);
        boolean frontStop = false, endStop = false;
        while (frontIndex - endIndex < urls[0].length() - 1) {
            for (String s : urls) {
                if (frontC != s.charAt(frontIndex)) {
                    frontIndex--;
                    frontStop = true;
                    break;
                }
            }
            frontIndex++;
            frontC = urls[0].charAt(frontIndex);

            for (String s : urls) {
                if (endC != s.charAt(s.length() + endIndex)) {
                    endIndex++;
                    endStop = true;
                    break;
                }
            }
            endIndex--;
            endC = urls[0].charAt(urls[0].length() + endIndex);

            if (frontStop && endStop)
                break;
        }
        endIndex++;
        String model = urls[0].substring(0, frontIndex) + "%s" + urls[0].substring(urls[0].length() + endIndex + 1);
        for (int i=0; i<urls.length; i++)
            urls[i] = urls[i].substring(frontIndex, urls[i].length() + endIndex);

        return model;
    }
}
