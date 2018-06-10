package com.kostenko.mergesort.sorters;

import com.kostenko.mergesort.Sort;

public class SimpleSort implements Sort {

    @Override
    public void sort(int[] array) {
        if (isNeedSort(array)) {
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array.length; j++) {
                    if (array[j] > array[i]) {
                        int temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        }
    }
}