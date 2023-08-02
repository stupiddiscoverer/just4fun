package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.TextUtiil.deciNumLen;
import static utils.TextUtiil.getCurrentDir;
import static utils.Utilities.*;

public class FileUtil {
    public static void writeStrToFile (String path, String content) {
        writeStrToFile(path, content, false);
    }
    public static void log(String content) {
        System.out.println(content);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(new Date());
        FileUtil.writeStrToFile("E:/log/" + now.substring(0, 10) + ".txt", now.substring(11) + ": " + content
                + "\n", true);
    }

    public static void writeBytesToFile(byte[] bytes, String fileName) {
        if (bytes == null) {
            System.out.println("bytes数据为空");
            return;
        }
        try {
            File dir = new File(getCurrentDir(fileName));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file,false);
            os.write(bytes);
            os.close();
            System.out.println("写入byte成功" + fileName);
        } catch (IOException e) {
            System.out.println("写入byte失败" + fileName);
        }
    }

    public static void writeStrToFile (String path, String content, boolean append) {
        try {
            path = path.replace("\\", "/");
            File dir = new File(getCurrentDir(path));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path);
            if(file.exists())
                System.out.println("文件已存在！！！");
            else {
                if (!file.createNewFile())
                    System.out.println("创建文件失败！！！");
            }
            FileWriter fileWriter = new FileWriter(path, append);
            fileWriter.write(content);
            fileWriter.close();
            System.out.println("写入string成功" + path);
        } catch (IOException e) {
            System.out.println("写入string失败" + path);
        }
    }

    public static String readStrFromFile(String path) {
        return readStrFromFile(path, "utf-8");
    }

    public static String readStrFromFile(String path, String charset) {
        String content = "";
        byte[] array = readBytesFromFile(path);
        if (array == null)
            return content;

        if (isEmpty(charset))
            charset = "utf-8";
        try {
            content = new String(array, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static byte[] readBytesFromFile(String path) {
        try {
            File file =new File(path);
            if(!file.exists()) {
                System.out.println("文件不存在！！！");
                return null;
            }
            int length = (int)file.length();
            byte[] array = new byte[length];
            FileInputStream fileInputStream = new FileInputStream(path);
            System.out.println("读取文件返回值为：" + fileInputStream.read(array));
            return array;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeArrayToFile (String path, long[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = array.length;
        int maxLen = deciNumLen(maxAbsNum(array));
        for (int i=0; i<len;) {
            stringBuilder.append(array[i]);
            for (int j=deciNumLen(array[i]); j<maxLen; j++)
                stringBuilder.append(" ");
            stringBuilder.append("\t");
            i++;
            if (i % 10 == 0)
                stringBuilder.append("\n");
        }
        System.out.println("array build String success");
        writeStrToFile(path, stringBuilder.toString());
    }
    public static void writeArrayToFile (String path, int[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = array.length;
        int maxLen = deciNumLen(maxAbsNum(array));
        for (int i=0; i<len;) {
            stringBuilder.append(array[i]);
            for (int j=deciNumLen(array[i]); j<maxLen; j++)
                stringBuilder.append(" ");
            stringBuilder.append("\t");
            i++;
            if (i % 10 == 0)
                stringBuilder.append("\n");
        }
        System.out.println("array build String success");
        writeStrToFile(path, stringBuilder.toString());
    }

    public static int[] readArrayFromFile(String path) {
        LinkedList<Integer> list = new LinkedList<>();
        try {
            String content = new String(readBytesFromFile(path), "utf-8");
            Matcher matcher = Pattern.compile("\\d+").matcher(content);
            while (matcher.find())
                list.add(Integer.valueOf(matcher.group()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int length = list.size();
        int[] array = new int[length];
        int index = 0;
        for (int i : list) {
            array[index] = i;
            index++;
        }
        return array;
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
    public static String[] getAllFiles(String dir) {
        File file = new File(dir);
        if (!file.isDirectory())
            return null;
        LinkedList<File> files = new LinkedList<>();
        getAllFiles(file, files);
        String[] fileNames = new String[files.size()];
        int index = 0;
        for (File f : files) {
            fileNames[index] = f.getPath();
            index++;
        }
        return fileNames;
    }
}
