package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class minStr {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) { // 注意 while 处理多个 case
            String a = in.nextLine();
            char[] chars = a.toCharArray();
            ArrayList<Character> characters = new ArrayList<>(chars.length);
            for (char c : chars)
            {
                characters.add(c);
            }
            Comparator comparator = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return Character.compare((Character) o1, (Character)o2);
                }
            };
            characters.sort(comparator);
            for (int i=0; i<chars.length; i++)
            {
                if (chars[i] != characters.get(i)){
                    int end = endIndex(chars, characters.get(i));
                    char c = chars[i];
                    chars[i] = characters.get(i);
                    chars[end] = c;
                    break;
                }
            }
            System.out.println(chars);
        }
    }
    static int endIndex(char[] chars, char c)
    {
        for (int i=chars.length - 1; i>=0; i--){
            if(chars[i] == c){
                return i;
            }
        }
        return 0;
    }
}
