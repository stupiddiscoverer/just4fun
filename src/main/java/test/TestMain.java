package test;

import annotationAndReflect.Serialize;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import questions.ParseGrammar;
import sortUtil.Sort;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Log4j2
@Serialize
public class TestMain {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(ParseGrammar.parseMath("(((9-5|2)+3)^2)^2 - 5*4"));
        String line = "<sdf>shit..</sdf>";
        String name = line.substring(line.indexOf('>') + 1, line.indexOf('<', 5));
        System.out.println(name);
        String shit = line + "dddd";
        System.out.println(shit);
    }

    public static <T> String getClassName(T object) {
        return object.getClass().getName();
    }

    private static void test1() {
        Sort sort = new Sort();
        Logger logger = LogManager.getLogger(TestMain.class);
//        logger.info("shit");
        log.info("shit");
        Class<Sort> sortClass = Sort.class;
        try {
            Method methodSort = sortClass.getMethod("quickSort", int[].class);
            getMethodAnnotation(methodSort);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void getMethodAnnotation(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
    }

}

