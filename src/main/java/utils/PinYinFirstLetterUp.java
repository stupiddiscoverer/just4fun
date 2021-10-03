package utils;

public class PinYinFirstLetterUp {
    //声母后必接韵母，韵母后必接声母，这就是拼音，除了a，o，e这种单个字母的拼音
    //一个拼音结束，下一个字母一定是声母或者无需声母的韵母a,o,e,en,ao,ai,ou,an
    private static char[] singleVowels = {'a', 'o', 'e'};
    private static String[] twoVowels = {"ao", "an", "ai", "ou", "en", "ei", "er"};
    private static char[] consonants = {'b', 'p', 'm', 'f', 'd', 't', 'n', 'l', 'g', 'k', 'h', 'j', 'q', 'x', 'r',
            'z', 'c', 's', 'w', 'y'};
    private static char[] vowels = {'a', 'o', 'e', 'i', 'u', 'v'};
    private static String[] compoundConsonants = {"zh", "ch", "sh", "ti"};
    private static String[] twoCompoundVowels = {"ai", "ei", "ui", "ao", "ou", "iu", "ie", "ve", "an", "en", "in", "un",
            "vn", "uo"};
    private static String[] threeCompoundVowels = {"ang", "eng", "ing", "ong"};

    private static int wordsNumMin = -1;      //省份最少有两个字，即xian应转为XiAn而不是Xian，只会发生在双韵母的情形下，设为负数怎不算这个规则

    public static String transform(String input) {
        char[] charArray = input.toLowerCase().toCharArray();
        int len = charArray.length;
        int remainLen = 0;  //剩余长度小于3时肯定不用匹配3个字母的韵母
        char c;
        boolean endOfAWord = true;
        int words = 0;

        for (int i=0; i<len; i++) {
            if (wordsNumMin > 0 && endOfAWord)
                words++;
            c = charArray[i];
            if (!isLowerLetter(c)) {     //不是字母，代表结束
                if (words > 0 && words < wordsNumMin + 1) {   //尝试从前面找tian，xian这种拼音并把Xian变为XiAn
                    XianToXiAn(charArray, i, words);
                }
                words = 0;
                endOfAWord = true;
                continue;
            }
            remainLen = len - i;
            if (!endOfAWord && remainLen > 2 && startsWithOneOfStrings(i, charArray, threeCompoundVowels)) {   //3个的一定是拼音结尾
                i += 2;
                endOfAWord = true;      //韵母表示一个字的拼音结束
                continue;
            }
            if (!endOfAWord && remainLen > 1 && startsWithOneOfStrings(i, charArray, twoCompoundVowels)) {     //2个的也一定是拼音结尾
                i += 1;
                endOfAWord = true;      //韵母表示一个字的拼音结束
                if (containsChar(charArray[i+1], vowels) && (i+3 < len && !startsWithOneOfStrings(i+1, charArray, twoVowels))) {
                    i--;
                }
                continue;
            }
            if (endOfAWord && remainLen > 2 && startsWithOneOfStrings(i, charArray, compoundConsonants)) {
                charArray[i] = toUpperCase(c);
                i += 1;
                endOfAWord = false;     //声母表示没有结束
                continue;
            }
            if (endOfAWord && remainLen > 1 && containsChar(c, consonants)) {
                charArray[i] = toUpperCase(c);
                endOfAWord = false;     //声母表示没有结束
                continue;
            }
            if (endOfAWord && remainLen > 1 && startsWithOneOfStrings(i, charArray, twoVowels)) {
                charArray[i] = toUpperCase(c);
                i += 1;
                endOfAWord = true;      //两字母韵母拼音，an,ao,en...拼音结束
                continue;
            }
            if (endOfAWord && containsChar(c, singleVowels)) {  //单字拼音一定出现在上一个字拼音结束时
                charArray[i] = toUpperCase(c);
                endOfAWord = true;      //单字母拼音，拼音结束
                continue;
            }
            if (!endOfAWord && containsChar(c, vowels)) {   //单韵母，后面可能还有韵母，比如shuang，tian
                endOfAWord = true;
                int k = i + 1;
                if (remainLen > 3 && startsWithOneOfStrings(k, charArray, compoundConsonants))
                    continue;
                if (remainLen > 2 && containsChar(charArray[k], consonants))
                    continue;
                if (remainLen > 3 && startsWithOneOfStrings(k, charArray, threeCompoundVowels)) {
                    i += 3;
                    continue;
                }
                if (remainLen > 2 && startsWithOneOfStrings(k, charArray, twoCompoundVowels) && !containsChar(c, singleVowels)) {
                    i += 2;
                    if (i == len-1) {
                        words++;
                        if (words < wordsNumMin + 1) {   //尝试从前面找tian，xian这种拼音并把Xian变为XiAn
                            XianToXiAn(charArray, i+1, words);
                        }
                    }
                    continue;
                }
                if (remainLen > 1 && containsChar(charArray[k], vowels)) {
                    i += 1;
                    if (i == len-1) {
                        words++;
                        if (words < wordsNumMin + 1) {   //尝试从前面找tian，xian这种拼音并把Xian变为XiAn
                            XianToXiAn(charArray, i+1, words);
                        }
                    }
                }
            }
        }
        return new String(charArray);
    }

    public void setWordsNumMin(int count) {
        wordsNumMin = count;     //省份应当设为2
    }

    private static void XianToXiAn(char[] charArray, int i, int words) {
        int j = i - 1;
        while (j >= 0 && isLetter(charArray[j]))
            j--;
        j++;
        int remainL = i - j;
        while (words < wordsNumMin + 1 && remainL > 2) {      //xia变为XiA，最少剩下3个字母
            while (isUpperLetter(charArray[j]))
                j++;
            remainL = i - j;
            if (containsChar(charArray[j], vowels)) {
                j++;
                if (remainL > 1 && i < charArray.length && startsWithOneOfStrings(j, charArray, twoVowels)) {
                    charArray[j] = toUpperCase(charArray[j]);
                    charArray[j+1] = toLowerCase(charArray[j+1]);
                    j += 2;
                    remainL = i - j;
                    words++;
                    continue;
                }
                if (remainL > 0 && containsChar(charArray[j], singleVowels)) {
                    charArray[j] = toUpperCase(charArray[j]);
                    words++;
                    j++;
                }
                remainL = i - j;
            }
        }
    }
    //97--122小写   65--90大写
    public static boolean isLetter(char c) {
        return (c >= 97 && c <= 122) || (c >= 65 && c <= 90);
    }

    public static boolean isUpperLetter(char c) {
        return c >= 65 && c <= 90;
    }

    public static boolean isLowerLetter(char c) {
        return c >= 97 && c <= 122;
    }

    public static boolean isNum(char c) {
        return c >= 48 && c <= 57;
    }

    private static boolean startsWithOneOfStrings(int i, char[] chars, String[] strings) {
        for (String s : strings)
            if (s.length() == 3) {
                if (toLowerCase(chars[i]) == s.charAt(0) && toLowerCase(chars[i + 1]) == s.charAt(1) &&
                        toLowerCase(chars[i + 2]) == s.charAt(2))
                    return true;
            } else {
                if (toLowerCase(chars[i]) == s.charAt(0) && toLowerCase(chars[i + 1]) == s.charAt(1))
                    return true;
            }
        return false;
    }

    //97--122小写   65--90大写
    public static char toUpperCase(char c) {
        if (97 <= c && c <= 122)
            return (char)(c - 32);
        return c;
    }

    public static char toLowerCase(char c) {
        if (65 <= c && c <= 90)
            return (char)(c + 32);
        return c;
    }

    public static boolean containsChar(char c, char[] chars) {
        c = toLowerCase(c);
        for (char c1 : chars)
            if (c == c1)
                return true;
        return false;
    }
}