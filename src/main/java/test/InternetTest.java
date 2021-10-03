package test;

import utils.AnyToAnySystem;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class InternetTest {
    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostName = inetAddress.getHostName();   //计算机名
            String ip = inetAddress.getHostAddress();   //ip地址
            byte[] bytes = inetAddress.getAddress();
            System.out.println("hostName = " + hostName + "\tip = " + ip + "\t" + inetAddress.getAddress());
            System.out.println(bytes.length + new String(bytes));
            for (int i=0; i<bytes.length; i++) {
                System.out.print(AnyToAnySystem.toBinary(bytes[i]) + "\t" + AnyToAnySystem.getUnsignedByteValue(bytes[i]) + "\t");
            }
            URL baidu =new URL("http://www.baidu.com");
            InputStream inputStream = baidu.openStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            reader.close();
            inputStream.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
