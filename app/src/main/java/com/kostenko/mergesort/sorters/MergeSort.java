package com.kostenko.mergesort.sorters;

import com.kostenko.mergesort.Merge;
import com.kostenko.mergesort.Sort;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

public class MergeSort implements Sort, Merge {

    @Override
    public void sort(int[] array) {
        if (isNeedSort(array)) {
            mergeSort(array);
        }
    }

    private void mergeSort(int[] inputArray) {
        int inputLength = inputArray.length;
        for (int i = 1; i < inputLength; i *= 2) {
            for (int shift = 0; shift < inputLength; shift += i * 2) {
                if (shift + i >= inputLength) break;
                int secondArrayLength = (shift + i * 2 > inputLength) ? (inputLength - (shift + i)) : i;
                int[] secondArray = merge(copyOfRange(inputArray, shift, shift + i), copyOfRange(inputArray, shift + i, shift + i + secondArrayLength));
                arraycopy(secondArray, 0, inputArray, shift, i + secondArrayLength);
            }
        }
    }
}
