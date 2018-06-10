package com.kostenko;

public class MyAssert {
    public static void assertEquals(Object first, Object second) {
        if (first.equals(second)) return;
        throw new RuntimeException("Assert fail: argument 1: " + first + "argument 2: " + second);
    }

    public static void assertEquals(long first, long second) {
        if (first == second) return;
        throw new RuntimeException("Assert fail: argument 1: " + first + "argument 2: " + second);
    }

    public static void assertEquals(int first, int second) {
        if (first == second) return;
        throw new RuntimeException("Assert fail: argument 1: " + first + "argument 2: " + second);
    }

    public static void assertTrue(boolean bool) {
        if (bool) return;
        throw new RuntimeException("Assert fail: argument: " + bool);
    }
}
