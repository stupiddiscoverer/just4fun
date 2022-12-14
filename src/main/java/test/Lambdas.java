package test;

import sortUtil.Sort;
import sortUtil.ParaSort;
import utils.TextUtiil;
import utils.Utilities;

import java.util.*;
import java.util.stream.Collectors;

public class Lambdas {

    /**
     * Java8重要特性之Lambda表达式:
     *      Lambda表达式可以理解为一种可传递的匿名函数：它没有名称，但有参数列表、函数主体、返回类型、可能还有一个可以抛出的异常列表
     */

    public static void main(String[] args) {
        sortTest();

    }

    private static void sortTest() {
        int mount = 20000;
        int len = 1000 * mount;

        int[] arr = getRandomArr(len);
        int[] copy = arr.clone();
        ArrayList<Integer> array = (ArrayList<Integer>) Arrays.stream(arr).boxed().collect(Collectors.toList());


        long time = new Date().getTime();
        Sort.quickSort(arr);
        System.out.println("快速排序耗时：" + (new Date().getTime() - time) + "毫秒");

        time = new Date().getTime();
        Collections.sort(array, Comparator.naturalOrder());
        System.out.println("Collection排序耗时：" + (new Date().getTime() - time) + "毫秒");

        time = new Date().getTime();
//        Sort.mergeSort(copy);
        ParaSort.sort(copy, 6);
        System.out.println("归并排序耗时：" + (new Date().getTime() - time) + "毫秒");


        TextUtiil.printArray(arr);
        TextUtiil.printArray(array);
        TextUtiil.printArray(copy);
    }

    public static int[] getRandomArr(int len) {
        System.out.println("长度" + len);
        Random random = new Random(new Date().getTime());
        int[] arr = new int[len];

        for (int i=0; i<len; i++) {
            arr[i] = random.nextInt() ;
        }

        return arr;
    }

    private void stupidTest() {
        /**
         * Java8对排序的改变
         */
        List<String> names = Arrays.asList("weijlu", "luweijie", "rovger", "anna", "peter");
        //Java7及以前的List排序实现是下面这种
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("Java7按字母顺序："+ names.toString());

        //Java8排序写法如下
        Collections.sort(names, (o1, o2) -> o2.compareTo(o1));
//        Collections.sort(names, Comparator.reverseOrder());
        System.out.println("Java8按字母倒序："+ names.toString());

        /**
         * Stream接口
         */
        names.stream()
                .filter(s -> s.startsWith("a"))
                .forEach(System.out::println);

        //利用stream来处理List
        names.stream().forEach(s -> System.out.println(s));

        //遍历Map
        Map<String, Integer> items = new HashMap<>();
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);
        items.forEach((k, v) -> {
            System.out.println("k: "+ k +", v: "+ v);
            if ("E".equals(k)) {
                System.out.println("Hello E");
            }
        });


        /**
         * Java8对线程coding的改变
         */
        //Java7及以前线程写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Before Java8");
            }
        }).start();

        //Java8的coding风格
        new Thread(() -> System.out.println("In Java8")).start();


        /**
         * Lambda表达式的
         * map：允许改变对象，将一下list中所有元素改变他们的数值，通过map方法可以将指定计算表达式应用到所有stream中所有元素
         * reduce：将集合中所有元素集合到一起，reduce有类似于SQL语句中的sum()，avg()，count()
         */
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax.stream()
                .map((cost) -> cost + cost*.12)
                .forEach(System.out::println);

        double total = costBeforeTax.stream()
                .reduce((sum, cost) -> sum + cost)
                .get();
        System.out.println("Total:"+ total);
    }
}
