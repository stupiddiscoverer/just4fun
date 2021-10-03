package utils;

import java.util.Arrays;

public class AnyToAnySystem {
    public static void main(String[] args) {
//        deciToOther(5613, 31);
//        otherToDeci("12", 11);
        anyToAny("1d", 14, 3);
        //161--255各种符合，，48--57 0-9，，33--47各种符合，，65--90 A-Z，，97--122 a-z，，91--96各种符合，，123--126各种符合
        //ascii码共有233个非空字符
        //而可辨认的字符只有上面的符合数字和字母共182个
        //所以进制最多到十个数字加26字母36个吧
    }

    static final int MAX_SCALE = 36;
    static final int MIN_SCALE = 2;
    static char[] symbols;

    private static boolean initialSymbols(int scale) {
        if (scale < MIN_SCALE || scale > MAX_SCALE) {
            System.out.println("只支持2到36进制");
            return false;
        }
        symbols = new char[scale];
        if (scale <= 10)
            for (int i=0; i<scale; i++)
                symbols[i] = (char) (48 + i);
        if (scale > 10) {
            for (int i=0; i<10; i++)
                symbols[i] = (char) (48 + i);
            for (int i=10; i<scale; i++)
                symbols[i] = (char) (87 + i);
            System.out.println(scale + "进制最大字符为：" + (char)(86 + scale));
        }
        return true;
    }

    public static String deciToOther (int decimal, int scale) {
        if (!initialSymbols(scale))
            return "";
        if (decimal == 0) {
            System.out.println("任何进制的0对应的任何进制都是0");
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        while (decimal > 0) {
            builder.append(symbols[decimal % scale]);
            decimal = decimal / scale;
        }
        builder.reverse();
        return builder.toString();
    }

    public static int binaryToDeci(String binary) {
        return Integer.parseInt(binary, 2);
    }

    public static int otherToDeci (String value, int scale) {
        if (scale == 10)
            return Integer.parseInt(value);
        char[] array = value.toLowerCase().toCharArray();
        int decimal = 0;
        for (char c : array) {
            decimal *= scale;
            if (c <= 57) {
                if (c < 48 || c - 48 >= scale) { //n进制最大个位数为n-1
                    System.out.println("字符：" + c + "超出" + scale + "进制的范围！！");
                    return 0;   //
                }
                decimal += c - 48;
            }
            else {
                if (c > 122 || c - 87 >= scale) {
                    System.out.println("字符：" + c + "超出" + scale + "进制的范围！！");
                    return 0;   //
                }
                decimal += c - 87;
            }
        }
        return decimal;
    }

    public static String anyToAny(String value, int scaleBefore, int scaleAfter) {
        return deciToOther(otherToDeci(value, scaleBefore), scaleAfter);
    }

    public static int getComplementCodeValue(String binary) {
        char[] values = binary.toCharArray();
        int deci = otherToDeci(binary, 2);
        if (values[0] == '0') {
            return deci;
        } else {
            char[] up = new char[values.length + 1];
            up[0] = '1';
            for (int i=1; i<up.length; i++) {
                up[i] = '0';
            }
            return deci - otherToDeci(new String(up), 2);
        }
    }

    public static String toBinary(char c) {
        if (c < 0b100000000) {
            return toBinary((byte) c);
        } else {
            return toBinary( (byte)(char) (c >> 8)) + toBinary((byte) c);
        }
    }

    public static int getUnsignedByteValue(byte b) {
        if (b < 0) {
            return otherToDeci(toBinary(b), 2);
        }
        return b;
    }

    public static String toBinary(byte b) {
        char[] value = new char[8];
        byte b1 = 0b1000000;
        if (b < 0) {
            value[0] = '1';
        } else {
            value[0] = '0';
        }
        for (int i=1; i<8; i++) {
            if ((b & b1) == b1)
                value[i] = '1';
            else
                value[i] = '0';
            b1 = (byte) (b1 >> 1);
        }

        return new String(value);
    }
}
