package lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.FileUtils.getInputDataFromFile;

public class Lab1_1 {

    public static final String LAB_1_INPUT_TXT = "src/main/resources/lab1/input.txt";
    public static final String UNEXPECTED_VALUES = "unexpected values";

    public static void main(String[] args) {
        var inputData = getInputDataFromFile(LAB_1_INPUT_TXT);

        inputData.forEach(System.out::println);

        var lineViaWaldCriteria = getLineViaWaldCriteria(inputData);

        System.out.println("\nResult of Wald criteria = " + lineViaWaldCriteria.toString() + "\n\n");

        var lineViaLaplaceCriteria = getLineViaLaplaceCriteria(inputData);

        System.out.println("Result of Laplace criteria = " + lineViaLaplaceCriteria.toString() + "\n\n");

        var coefficient = 0.8;
        var byHyrwitzCriteria = getLineViaHyrwitzCriteria(inputData, coefficient);

        System.out.println("Result of Hyrwitz criteria with coefficient = " + coefficient + ": " + byHyrwitzCriteria.toString() + "\n\n");

        coefficient = 0.2;
        byHyrwitzCriteria = getLineViaHyrwitzCriteria(inputData, coefficient);

        System.out.println("Result of Hyrwitz criteria with coefficient = " + coefficient + ": " + byHyrwitzCriteria.toString() + "\n\n");
    }

    private static List<Integer> getLineViaHyrwitzCriteria(List<List<Integer>> inputData, double coefficient) {
        var minRowValues = getMinRowValues(inputData);
        var maxRowValues = getMaxRowValues(inputData);

        System.out.println("minRowValues = " + minRowValues.toString());
        System.out.println("maxRowValues = " + maxRowValues.toString());


        var resultList = new ArrayList<Double>();
        System.out.println("(k*min + (1-k)*max)");
        for (int i = 0; i < inputData.size(); i++) {
            resultList.add(coefficient * minRowValues.get(i) + (1 - coefficient) * maxRowValues.get(i));
        }

        System.out.println("The values calculated for each row: " + resultList.toString());

        var index = 0;
        var maxValue = 0.0;
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i) > maxValue) {
                maxValue = resultList.get(i);
                index = i;
            }
        }

        return inputData.get(index);
    }

    private static List<Integer> getLineViaLaplaceCriteria(List<List<Integer>> inputData) {
        var sumOfRows = new HashMap<Integer, Double>();

        var amountOfColumns = inputData.stream()
                .map(List::size)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));

        for (int i = 0; i < inputData.size(); i++) {
            var sum = inputData.get(i).stream()
                    .reduce(Integer::sum)
                    .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));
            System.out.println("Sum for row: " + sum);
            sumOfRows.put(i, (double) sum / amountOfColumns);
        }

        System.out.println("Divided values = " + sumOfRows.toString());

        var entryWithTheHighestKey = getValueWithTheSmallestKey(sumOfRows);

        return inputData.get(entryWithTheHighestKey.getKey());
    }

    private static Map.Entry<Integer, Double> getValueWithTheSmallestKey(HashMap<Integer, Double> sumOfRows) {
        return sumOfRows.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));
    }

    private static List<Integer> getLineViaWaldCriteria(List<List<Integer>> inputData) {
        var minRowValues = getMinRowValues(inputData);
        var valueWithTheSmallestKey = getValueWithTheSmallestKey(minRowValues);
        return inputData.get(valueWithTheSmallestKey.getKey());
    }

    private static HashMap<Integer, Double> getMinRowValues(List<List<Integer>> inputData) {
        var result = new HashMap<Integer, Double>();
        for (int i = 0; i < inputData.size(); i++) {
            var maxMinValueOfTheRow = inputData.get(i).stream()
                    .min(Integer::compareTo)
                    .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));
            result.put(i, (double) maxMinValueOfTheRow);
        }
        return result;
    }

    private static HashMap<Integer, Integer> getMaxRowValues(List<List<Integer>> inputData) {
        var result = new HashMap<Integer, Integer>();
        for (int i = 0; i < inputData.size(); i++) {
            var maxMinValueOfTheRow = inputData.get(i).stream()
                    .max(Integer::compareTo)
                    .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));
            result.put(i, maxMinValueOfTheRow);
        }
        return result;
    }
}