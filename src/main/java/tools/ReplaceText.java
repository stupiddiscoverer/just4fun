package tools;
import utils.FileUtil;

import java.io.*;
import java.util.LinkedList;

import static utils.FileUtil.readBytesFromFile;
import static utils.FileUtil.writeStrToFile;

public class ReplaceText {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("请输入： 文件名 替换前的字符串 替换后的字符串");
            return;
        }
        String path = args[0];
        String before = args[1];
        String after = args[2];
        path = path.replace("\\", "/");
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (path.endsWith("/"))
                    file.mkdir();
                else
                    file.createNewFile();
                System.out.println("文件不存在，已创建");
                return;
            } catch (IOException e) {
                System.out.println("文件不存在，创建失败");
                e.printStackTrace();
            }
        }
        if (file.isDirectory()) {
            LinkedList<File> files = new LinkedList<>();
            FileUtil.getAllFiles(file, files);
            for (File f : files) {
                path = f.getPath();
                replace(path, before, after);
            }
        }
        if (file.isFile()){
            replace(path, before, after);
        }
    }
    
    public static void replace(String path, String before, String after) {
        String content = readStrFromFile(path);
        System.out.print(path);
        if (content.contains(before)) {
            System.out.print("  \tcontains: " + before + "  \t");
            content = content.replace(before, after);
            writeStrToFile(path, content);
        }
        System.out.println();
    }

    public static String readStrFromFile(String path) {
        return readStrFromFile(path, "utf-8");
    }

    public static String readStrFromFile(String path, String charset) {
        String content = "";
        byte[] array = readBytesFromFile(path);
        if (array == null)
            return content;

        if (charset == null || charset.trim().isEmpty())
            charset = "utf-8";
        try {
            content = new String(array, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }


}
