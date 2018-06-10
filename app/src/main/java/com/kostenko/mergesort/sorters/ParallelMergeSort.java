package com.kostenko.mergesort.sorters;

import com.kostenko.mergesort.Merge;
import com.kostenko.mergesort.Sort;

import java.util.concurrent.RecursiveAction;

import static java.lang.Math.floorDiv;
import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;

public class ParallelMergeSort extends RecursiveAction implements Sort, Merge {
    private final int sizeToDoInParallel;
    private int[] inputArray;
    private int leftIndex;
    private int rightIndex;

    public ParallelMergeSort(int sizeToDoInParallel) {
        this.sizeToDoInParallel = sizeToDoInParallel;
    }

    private ParallelMergeSort(int[] inputArray, int leftIndex, int rightIndex, int sizeToDoInParallel) {
        this.sizeToDoInParallel = sizeToDoInParallel;
        this.inputArray = inputArray;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }

    @Override
    protected void compute() {
        if (leftIndex < rightIndex) {
            int size = rightIndex - leftIndex;
            if (size < sizeToDoInParallel) {
                new InsertSort(leftIndex, rightIndex).sort(inputArray);
            } else {
                int mid = leftIndex + floorDiv(size, 2);
                invokeAll(new ParallelMergeSort(inputArray, leftIndex, mid, sizeToDoInParallel), new ParallelMergeSort(inputArray, mid + 1, rightIndex, sizeToDoInParallel));
                int[] left = copyOfRange(inputArray, leftIndex, mid + 1);
                int[] right = copyOfRange(inputArray, mid + 1, rightIndex + 1);
                int[] merge = merge(left, right);
                arraycopy(merge, 0, inputArray, leftIndex, merge.length);
            }
        }
    }

    @Override
    public void sort(int[] array) {
        if (isNeedSort(array)) {
            this.inputArray = array;
            this.leftIndex = 0;
            this.rightIndex = array.length - 1;
            compute();
        }
    }
}
