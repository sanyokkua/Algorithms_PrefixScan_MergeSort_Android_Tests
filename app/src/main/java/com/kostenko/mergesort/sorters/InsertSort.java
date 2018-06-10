package com.kostenko.mergesort.sorters;

import com.kostenko.mergesort.Sort;

public class InsertSort implements Sort {
    private int leftIndex;
    private int rightIndex;
    private int[] inputArray;

    public InsertSort() { //TODO: bad idea, need to refactor
        this.leftIndex = 0;
        this.rightIndex = 0;
    }

    public InsertSort(int leftIndex, int rightIndex) {
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    @Override
    public void sort(int[] array) {
        if (isNeedSort(array)) {
            if (leftIndex == rightIndex) { //TODO: bad idea, need to refactor
                leftIndex = 0;
                rightIndex = array.length-1;
            }
            this.inputArray = array;
            insertionSort();
        }
    }

    private void insertionSort() {
        for (int i = leftIndex + 1; i <= rightIndex; ++i) {
            int current = inputArray[i];
            int j = i - 1;
            while (leftIndex <= j && current < inputArray[j]) {
                inputArray[j + 1] = inputArray[j--];
            }
            inputArray[j + 1] = current;
        }
    }
}
