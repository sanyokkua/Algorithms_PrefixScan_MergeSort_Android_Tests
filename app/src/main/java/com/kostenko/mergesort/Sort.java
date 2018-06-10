package com.kostenko.mergesort;

public interface Sort {
    void sort(int[] array);

    default boolean isNeedSort(int[] array) {
        return array != null && array.length > 1;
    }
}
