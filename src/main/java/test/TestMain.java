package test;

import sortUtil.Sort;
import utils.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class TestMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println((1<<31) - 1);
        String a = "阿里山的空间阿迪斯\uD83D\uDE22\uD83D\uDE03\uD83D\uDC40\n阿斯蒂芬哦❤( •̀ ω •́ )✧⑦▇¼₃₆₉₀⅓⅝½³⁷⁹";
        String[] test = {"asdoeizcasd", "asdwoifjsdkfjasd", "asdweoiznv/alsdkjweoijsd"};
        System.out.println(TextUtiil.removeSimpleFrontEnd(test));
        TextUtiil.printArray(test);
        Data data = new Data(new int[]{1,2,3}, test);
        Data data1 = data.clone();
        System.out.println(data1);
        System.out.println(data.equals(data1));
        System.out.println(data.nums.equals(data1.nums));
        System.out.println(data.contents.equals(test));
        System.out.println(data1.contents.equals(test));
        System.out.println(test);
        System.out.println(data.contents);
        System.out.println(data1.contents);
        System.out.println(data.nums);
        System.out.println(data1.nums);
    }

    public static <T> String getClassName(T object) {
        return object.getClass().getName();
    }

}

