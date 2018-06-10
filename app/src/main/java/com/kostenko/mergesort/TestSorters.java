package com.kostenko.mergesort;

import com.kostenko.MyAssert;
import com.kostenko.TestSuit;
import com.kostenko.TextWriter;
import com.kostenko.mergesort.sorters.InsertSort;
import com.kostenko.mergesort.sorters.MergeSort;
import com.kostenko.mergesort.sorters.ParallelMergeSort;
import com.kostenko.mergesort.sorters.SimpleSort;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

import static java.lang.String.format;
import static java.util.Arrays.copyOf;

public class TestSorters implements TestSuit {
    private TextWriter writer;

    public TestSorters() {
    }

    @Override
    public void runAllTests(TextWriter textWriter) {
        this.writer = textWriter;
        setup();
        testStaticDataSort();
        setup();
        testDynamicBigDataSort();
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int[] test1 = {2, 6, 3, 8, 5, 1, 4, 9, 7, 10, 14, 0, 11, 15, 13, 12};
    private static final int[] test2 = copyOf(test1, test1.length);
    private static final int[] test3 = copyOf(test1, test1.length);
    private static final int[] test4 = copyOf(test1, test1.length);
    private Sort SIMPLE;
    private Sort INSERT;
    private Sort MERGE;
    private Sort PARALLEL_MERGE;
    private boolean runSlowSorters;
    private int maxNumberOfElements;
    private int numberOfTries;

    private void setup() {
        maxNumberOfElements = 1_000_000;
        runSlowSorters = false;
        numberOfTries = 1000;

        SIMPLE = new SimpleSort();
        INSERT = new InsertSort();
        MERGE = new MergeSort();
        PARALLEL_MERGE = new ParallelMergeSort(128);
    }

    private void testStaticDataSort() {
        test(SIMPLE, test1);
        test(INSERT, test2);
        test(MERGE, test3);
        test(PARALLEL_MERGE, test4);
    }

    private void testDynamicBigDataSort() {
        for (int currentSize = 1000; currentSize <= maxNumberOfElements; currentSize *= 10) {
            int[] bigArraySimpleSort = RANDOM.ints(currentSize).toArray();
            int[] bigArrayInsertSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);
            int[] bigArrayMergeSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);
            int[] bigArrayMergeParallelSort = copyOf(bigArraySimpleSort, bigArraySimpleSort.length);
            if (runSlowSorters) {
                test(SIMPLE, bigArraySimpleSort);
                test(INSERT, bigArrayInsertSort);
            }
            test(MERGE, bigArrayMergeSort);
            test(PARALLEL_MERGE, bigArrayMergeParallelSort);
        }
    }

    private void test(final Sort sort, final int[] array) {
        String header = "____________________________________________________________";
        String className = sort.getClass().getSimpleName();
        String result = format("%s\nSorting arrays with size = %d, by %s algorithm\nNumber of tries: %s", header, array.length, className, numberOfTries);
        writer.println(result);

        List<Long> tryTime = new ArrayList<>(numberOfTries);
        for (int i = 0; i < numberOfTries; i++) {
            Instant before = Instant.now();
            sort.sort(array);
            Instant after = Instant.now();
            isSorted(array); //Asserts
            long millis = Duration.between(before, after).toMillis();
            tryTime.add(millis);
        }
        long timeMin = tryTime.stream().reduce(BinaryOperator.minBy(Long::compareTo)).get();
        long timeMax = tryTime.stream().reduce(BinaryOperator.maxBy(Long::compareTo)).get();
        double timeAvg = tryTime.stream().mapToLong(value -> value.longValue()).average().getAsDouble();
        String result2 = format("Sorted successfully.\nTime: min - %d millis, max - %d millis, avg - %s millis, avg - %s seconds\n%s", timeMin, timeMax, timeAvg, timeAvg / 1000, header);
        writer.println(result2);

    }

    private void isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            MyAssert.assertTrue(array[i] <= array[i + 1]);
        }
    }
}
