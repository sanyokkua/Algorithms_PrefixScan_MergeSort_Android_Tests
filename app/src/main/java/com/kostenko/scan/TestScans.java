package com.kostenko.scan;

import com.kostenko.MyAssert;
import com.kostenko.TestSuit;
import com.kostenko.TextWriter;
import com.kostenko.scan.interfaces.PrefixScan;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class TestScans implements TestSuit {
    private TextWriter writer;

    public TestScans() {
    }

    @Override
    public void runAllTests(TextWriter textWriter) {
        this.writer = textWriter;
        try {
            setup();
            testLinear();
            setup();
            testParallelOff();
            setup();
            testParallelOn();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static final BiFunction<Integer, Integer, Integer> PLUS = (first, second) -> first + second;
    private static final Integer[] testActual1 = new Integer[]{0, 1, 4, 6, 8, 11, 12, 13, 5, 7, 4, 9};
    private static final Integer[] testExpected1 = new Integer[]{0, 1, 5, 11, 19, 30, 42, 55, 60, 67, 71, 80};
    private static final Integer[] testActual2 = new Integer[]{1, 2, 3, 4, 5, 6};
    private static final Integer[] testExpected2 = new Integer[]{1, 3, 6, 10, 15, 21};
    private static final Integer[] testActual3 = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
    private static final Integer[] testExpected3 = new Integer[]{1, 3, 6, 10, 15, 21, 28, 36};
    private int numberOfRuns;
    private Integer[] testGeneratedInput_1_000;
    private Integer[] testGeneratedExpected_1_000;
    private Integer[] testGeneratedInput_10_000;
    private Integer[] testGeneratedExpected_10_000;
    private Integer[] testGeneratedInput_100_000;
    private Integer[] testGeneratedExpected_100_000;
    private Integer[] testGeneratedInput_1_000_000;
    private Integer[] testGeneratedExpected_1_000_000;

    private void setup() throws Exception {
        numberOfRuns = 1000;
        testGeneratedInput_1_000 = generateTestData(1_000);
        testGeneratedInput_10_000 = generateTestData(10_000);
        testGeneratedInput_100_000 = generateTestData(100_000);
        testGeneratedInput_1_000_000 = generateTestData(1_000_000);
        writer.println("Data generated");

        PrefixScan<Integer> prefixScanLinear = new PrefixScanLinear();

        testGeneratedExpected_1_000 = prefixScanLinear.compute(testGeneratedInput_1_000, PLUS);
        testGeneratedExpected_10_000 = prefixScanLinear.compute(testGeneratedInput_10_000, PLUS);
        testGeneratedExpected_100_000 = prefixScanLinear.compute(testGeneratedInput_100_000, PLUS);
        testGeneratedExpected_1_000_000 = prefixScanLinear.compute(testGeneratedInput_1_000_000, PLUS);
    }

    private void testLinear() throws Exception {
        writer.println("Begin linear tests:\n");
        PrefixScan<Integer> prefixScanLinear = new PrefixScanLinear();
        testData(prefixScanLinear, testActual1, testExpected1);
        testData(prefixScanLinear, testActual2, testExpected2);
        testData(prefixScanLinear, testActual3, testExpected3);
        testData(prefixScanLinear, testGeneratedInput_1_000, testGeneratedExpected_1_000);
        testData(prefixScanLinear, testGeneratedInput_10_000, testGeneratedExpected_10_000);
        testData(prefixScanLinear, testGeneratedInput_100_000, testGeneratedExpected_100_000);
        testData(prefixScanLinear, testGeneratedInput_1_000_000, testGeneratedExpected_1_000_000);
        writer.println("Finish linear tests");
    }

    private void testParallelOff() throws Exception {
        writer.println("Begin parallel off tests:");
        PrefixScan<Integer> prefixScanParallel = new PrefixScanParallel();
        testData(prefixScanParallel, testActual1, testExpected1);
        testData(prefixScanParallel, testActual2, testExpected2);
        testData(prefixScanParallel, testActual3, testExpected3);
        testData(prefixScanParallel, testGeneratedInput_1_000, testGeneratedExpected_1_000);
        testData(prefixScanParallel, testGeneratedInput_10_000, testGeneratedExpected_10_000);
        testData(prefixScanParallel, testGeneratedInput_100_000, testGeneratedExpected_100_000);
        testData(prefixScanParallel, testGeneratedInput_1_000_000, testGeneratedExpected_1_000_000);
        writer.println("Finish parallels off tests");
    }

    private void testParallelOn() throws Exception {
        writer.println("Begin parallel on tests:");
        PrefixScan<Integer> prefixScanParallel = new PrefixScanParallel(true, 4);
        testData(prefixScanParallel, testActual1, testExpected1);
        testData(prefixScanParallel, testActual2, testExpected2);
        testData(prefixScanParallel, testActual3, testExpected3);
        testData(prefixScanParallel, testGeneratedInput_1_000, testGeneratedExpected_1_000);
        testData(prefixScanParallel, testGeneratedInput_10_000, testGeneratedExpected_10_000);
        testData(prefixScanParallel, testGeneratedInput_100_000, testGeneratedExpected_100_000);
        testData(prefixScanParallel, testGeneratedInput_1_000_000, testGeneratedExpected_1_000_000);
        writer.println("Finish parallels on tests");
    }

    private void testData(PrefixScan<Integer> prefixScan, Integer[] input, Integer[] expected) throws Exception {
        List<Long> resultOfRuns = new ArrayList<>(numberOfRuns);
        for (int i = 0; i < numberOfRuns; i++) {
            Instant before = Instant.now();
            List<Integer> actualList = Arrays.asList(prefixScan.compute(input, PLUS));
            Instant after = Instant.now();
            List<Integer> expectedList = Arrays.asList(expected);
            MyAssert.assertEquals(expectedList.size(), actualList.size());
            compareByElement(actualList, expectedList, Arrays.asList(input));
            resultOfRuns.add(Duration.between(before, after).toMillis());
        }
        long timeMin = resultOfRuns.stream().reduce(BinaryOperator.minBy(Long::compareTo)).get();
        long timeMax = resultOfRuns.stream().reduce(BinaryOperator.maxBy(Long::compareTo)).get();
        double timeAverage = resultOfRuns.stream().mapToLong(Long::longValue).average().getAsDouble();
        String prefixScanName = prefixScan.getClass().getSimpleName();
        printResults(timeMin, timeMax, timeAverage, input.length, prefixScanName);
    }

    private void printResults(long timeMin, long timeMax, double timeAvg, int size, String prefixScanName) {
        String headers = "____________________________________________________________";
        String result = String.format("%s\nRunning by: %s, number of runs: %d, size: %d\nTime: min - %d millis, max - %d millis, avg %s millis\n%s", headers, prefixScanName, numberOfRuns, size, timeMin, timeMax, timeAvg, headers);
        writer.println(result);
    }

    private void compareByElement(List<Integer> actual, List<Integer> expected, List<Integer> input) {
        if (actual.size() != expected.size()) {
            throw new IllegalArgumentException("Sizes of Lists have to be equal");
        }
        //        writer.println("Input:");
        //        writer.println(input);
        //        writer.println("Actual:");
        //        writer.println(actual);
        //        writer.println("Expected:");
        //        writer.println(expected);
        //        writer.println("\n");
        for (int i = 0; i < actual.size(); i++) {
            MyAssert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    private Integer[] generateTestData(int numberOfElements) {
        Random random = new Random(System.currentTimeMillis());
        Integer[] result = new Integer[numberOfElements];
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt();
        }
        return result;
    }
}
