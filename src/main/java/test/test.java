package test;

//import jdk.nashorn.internal.ir.RuntimeNode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import utils.Utilities;

import java.util.ArrayList;
import java.util.Scanner;

public class test {
    static void tt(){
        String url = "https://arxiv.org/pdf/2403.07657.pdf";
        Request request = Utilities.getReq(url);
        Page page = Utilities.downloadPage(request);
        System.out.println(page.getBytes().length);
    }
    public static void main(String[] args) {
        tt();
    }

    static void record(ArrayList<ArrayList<Integer>> matrix, ArrayList<Integer> flags)
    {
        ArrayList<Integer> newFlag = new ArrayList<>();
        int index = 0;
        while (index < flags.size() - 1) {
            for (;index < flags.size(); index++) {
                int flag = flags.get(index);
                ArrayList<Integer> count = matrix.get(flag);
                for (int i = 0; i < count.size(); i++) {
                    if (i != flag && count.get(i) == 1) {
                        if (!flags.contains(i)) {
                            newFlag.add(i);
                        }
                    }
                }
            }
            flags.addAll(newFlag);
            newFlag.clear();
        }
    }

    static int parseInt(String a) {
        char[] b = a.trim().toCharArray();
        int i = 0;
        for (char c : b) {
            if (c <= '9' && c >= '0') {
                i = i * 10 + c - '0';
            }
            else
            {
                break;
            }
        }
        return i;
    }

    static void parseIntArr(String a, ArrayList<Integer> c) {
        char[] b = a.trim().toCharArray();
        int i = 0;
        boolean numBuilded = Boolean.FALSE;
        for (char d:b) {
            if (d <= '9' && d >= '0') {
                i = i * 10 + d - '0';
                numBuilded = Boolean.TRUE;
            }
            else
            {
                if (numBuilded)
                {
                    c.add(i);
                    i = 0;
                    numBuilded = Boolean.FALSE;
                }
            }
        }
        if (numBuilded) {
            c.add(i);
        }
    }
}
