package test;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Scanner;

public class test1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            int windowNum = parseInt(str);
            str = in.nextLine();
            int windowSize = parseInt(str);

            ArrayList<ArrayList<Integer>> arrs = new ArrayList<>();
            ArrayList<ArrayList<Integer>> winArrs = new ArrayList<>(windowNum);
            for (int i=0; i<windowNum; i++)
            {
                winArrs.add(new ArrayList<Integer>(windowSize));
            }

            int lineCount = 0;
            while (lineCount < 10) {
                str = in.nextLine();
                if (!str.isEmpty()) {
                    ArrayList<Integer> arr = new ArrayList<>();
                    parseIntArr(str, arr);
                    if (!arr.isEmpty())
                    {
                        arrs.add(arr);
                    }
                }
                lineCount++;
            }
            System.out.println("arrs is got");

            int arrIndex = 0; //第几个原始数组
            ArrayList<Integer> indexRecords = new ArrayList<>(arrs.size());
            int usedRecords = 0;
            for (int i=0; i<arrs.size(); i++)
            {
                indexRecords.add(0);
            }
            for (int i=0; i<windowSize; i++)
            {
                for (int j=0; j<windowNum; j++)
                {
                    int indexTemp = indexRecords.get(arrIndex); //取当前原始数组的下标
                    while (indexTemp >= arrs.get(arrIndex).size())
                    {
                        arrs.remove(arrIndex);
                        usedRecords = 0;
                        if (arrIndex >= arrs.size())
                        {
                            arrIndex = 0;
                        }
                        for (int k=arrIndex; k<indexRecords.size() - 1; k++){
                            indexRecords.set(k, indexRecords.get(k+1));
                        }
                        indexRecords.remove(indexRecords.size() - 1);

                        indexTemp = indexRecords.get(arrIndex);
                    }
                    winArrs.get(j).add(arrs.get(arrIndex).get(indexTemp));
                    usedRecords++;
                    indexRecords.set(arrIndex, indexTemp + 1);
                    if (usedRecords == windowNum)
                    {
                        arrIndex = (arrIndex + 1) % arrs.size();
                        usedRecords = 0;
                    }
                }

            }

            System.out.println("分配成功");
            for (int i=0; i<windowNum; i++) {
                for (int j = 0; j < windowSize; j++) {
                    System.out.print(winArrs.get(i).get(j));
                    System.out.print(' ');
                }
            }
            System.out.println();
            System.out.println("0 10 20 4 14 24 8 1 11 21 5 15 25 9 2 12 22 6 16 26 18 3 13 23 7 17 27 19");
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
