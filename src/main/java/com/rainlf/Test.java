package com.rainlf;

/**
 * @author : rain
 * @date : 2021/8/12 13:16
 */
public class Test {
    private static String xx;
    public static void main(String[] args) {

        System.out.println(1);
        synchronized (xx) {
            System.out.println(2);
        }
    }

}
