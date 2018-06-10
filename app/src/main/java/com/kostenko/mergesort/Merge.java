package com.kostenko.mergesort;

public interface Merge {

    default int[] merge(int[] left, int[] right) {
        int resultLength = left.length + right.length;
        int[] result = new int[resultLength];
        int li = 0;
        int ri = 0;
        for (int i = 0; i < resultLength; i++) {
            if (ri < right.length && li < left.length) {
                if (left[li] > right[ri]) {
                    result[i] = right[ri++];
                } else {
                    result[i] = left[li++];
                }
            } else if (ri < right.length) {
                result[i] = right[ri++];
            } else {
                result[i] = left[li++];
            }
        }
        return result;
    }
}
