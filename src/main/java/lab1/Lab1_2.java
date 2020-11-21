package lab1;

import java.util.Arrays;
import java.util.List;

import static lab1.Lab1_1.LAB_1_INPUT_TXT;
import static lab1.Lab1_1.UNEXPECTED_VALUES;
import static util.FileUtils.getIntegerInputDataFromFile;

public class Lab1_2 {

    public static final double[] lab1Coefficients = {0.5, 0.35, 0.15};

    public static void main(String[] args) {
        var inputData = getIntegerInputDataFromFile(LAB_1_INPUT_TXT);

        inputData.forEach(System.out::println);
        System.out.println();

        var lineViaByesLaplaceCriteria = getLineViaByesLaplaceCriteria(inputData);

        System.out.println("Result of Bayes Laplace = " + lineViaByesLaplaceCriteria.toString() + "\n\n");
    }

    private static List<Integer> getLineViaByesLaplaceCriteria(List<List<Integer>> inputData) {
        var amountOfColumns = inputData.stream()
                .map(List::size)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(UNEXPECTED_VALUES));
        double[] calculatedValues = new double[inputData.size()];
        System.out.println("(A11*k1 + A12*k2 + A13*k3)");
        for (int i = 0; i < inputData.size(); i++) {
            for (int j = 0; j < amountOfColumns; j++) {
                calculatedValues[i] += lab1Coefficients[j] * inputData.get(i).get(j);
            }
        }

        System.out.println("Values for each row: " + Arrays.toString(calculatedValues));

        var maxValue = 0.0;
        var maxIndex = 0;
        for (int i = 0; i < calculatedValues.length; i++) {
            if (calculatedValues[i] > maxValue) {
                maxValue = calculatedValues[i];
                maxIndex = i;
            }
        }
        return inputData.get(maxIndex);
    }
}
