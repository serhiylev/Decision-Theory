package lab2;

import com.google.gson.Gson;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Lab2 {

    public static final String LAB_2_INPUT_FILE_PATH = "src/main/resources/lab2/input.json";

    public static void main(String[] args) {
        TreeNode[] treeNodes = extractDataFromInputFile();

        var resultValues = calculateDecisionValuesForEachNode(treeNodes);
        var maxValue = Collections.max(resultValues);
        var indexOfMaxValue = resultValues.indexOf(maxValue);

        System.out.print("\n\nBest solution is: ");
        System.out.println(resultValues.get(indexOfMaxValue));
        System.out.println(treeNodes[indexOfMaxValue]);
    }

    private static List<Double> calculateDecisionValuesForEachNode(TreeNode[] treeEntities) {
        System.out.println("((annualIncome * incomeProbability + annualLoss * lossProbability) * years )/ factoryWorth\n");
        return Arrays.stream(treeEntities)
                .map(Lab2::findDecisionValue)
                .collect(Collectors.toList());
    }

    public static Double findDecisionValue(TreeNode node) {
        var annualIncome = node.getAnnualIncome();
        var incomeProbability = node.getIncomeProbability();
        var annualLoss = node.getAnnualLoss();
        var lossProbability = node.getLossProbability();
        var years = node.getYears();
        var factoryWorth = node.getFactoryWorth();

        var result = (annualIncome * incomeProbability + annualLoss * lossProbability) * years / factoryWorth;
        System.out.println(node.toString());
        System.out.println("Result =" + result);
        return result;
    }

    @SneakyThrows
    private static TreeNode[] extractDataFromInputFile() {
        var gson = new Gson();
        var pathToTheInputFile = Path.of(LAB_2_INPUT_FILE_PATH);
        var reader = Files.newBufferedReader(pathToTheInputFile);

        return gson.fromJson(reader, TreeNode[].class);
    }
}