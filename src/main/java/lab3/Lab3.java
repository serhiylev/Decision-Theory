package lab3;

import java.util.*;

import static util.FileUtils.getStringInputDataFromFile;

public class Lab3 {

    public static final String LAB_3_INPUT_FILE_PATH = "src/main/resources/lab3/input.txt";

    public static void main(String[] args) {

        var dataFromFile = getStringInputDataFromFile(LAB_3_INPUT_FILE_PATH);

        calculateWinnerViaCondorcetMethod(dataFromFile);

        calculateWinnerViaBordaMethod(dataFromFile);

    }

    private static void calculateWinnerViaBordaMethod(List<List<String>> dataFromFile) {
        var pointsPerCandidate = new HashMap<String, Integer>();

        var votes = extractVotes(dataFromFile);
        var candidateOrders = extractCandidates(dataFromFile);

        for (int i = 0; i < candidateOrders.size(); i++) {
            int finalI = i;
            for (int j = 0; j < candidateOrders.get(i).size(); j++) {
                int finalJ = j;
                pointsPerCandidate.compute(candidateOrders.get(i).get(j), (key, value) -> value == null ?
                        votes.get(finalI) * (candidateOrders.get(finalI).size() - (finalJ+1))
                        : value + (votes.get(finalI) * (candidateOrders.get(finalI).size() - (finalJ+1))));
            }
        }

        System.out.println(pointsPerCandidate);
        var result = pointsPerCandidate.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow(() -> new RuntimeException("There is no max value"));
        System.out.println("result = " + result);
    }

    private static void calculateWinnerViaCondorcetMethod(List<List<String>> dataFromFile) {

        System.out.println("Condorcet method");
        var votes = extractVotes(dataFromFile);
        var candidateOrders = extractCandidates(dataFromFile);

        var candidateVotes = new HashMap<String, Integer>();
        for (int i = 0; i < candidateOrders.size(); i++) {
            int finalI = i;
            var candidatesOrder = candidateOrders.get(i);
            for (int j = 0; j < candidatesOrder.size() - 1; j++) {
                for (int k = j + 1; k < candidatesOrder.size(); k++) {
                    candidateVotes.compute(candidatesOrder.get(j) + candidatesOrder.get(k),
                            (key, value) -> value == null ? votes.get(finalI) : value + votes.get(finalI));
                }
            }
        }

        System.out.println(candidateVotes);

        var maxEntry = candidateVotes.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("There is no max value"));


        var checkedValues = new HashSet<String>();

        var ifEverythingIsOk = findIfEverythingIsOk(candidateVotes, checkedValues, maxEntry.getKey());

        System.out.println("Result === " + ifEverythingIsOk);
    }

    // todo rename
    private static String findIfEverythingIsOk(HashMap<String, Integer> candidateVotes, HashSet<String> checkedValues, String maxEntry) {
        var valuesToCheck = new ArrayList<String>();
        for (String key : candidateVotes.keySet()) {
            if (key.startsWith(String.valueOf(maxEntry.toCharArray()[0])) && !key.equals(maxEntry)) {
                valuesToCheck.add(key);
            }
        }

        var allMatch = valuesToCheck.stream().allMatch(value -> value.equals(getCorrectPair(value, candidateVotes)));

        if (allMatch) {
            //cool
            return maxEntry;
        } else {
            checkedValues.add(maxEntry);
            var notSupportedValue = "";
            for (String value : valuesToCheck) {
                if (!value.equals(getCorrectPair(value, candidateVotes))) {//todo rewrite
                    notSupportedValue = getCorrectPair(value, candidateVotes);
                    //not cool, need to change
                }
            }
            return findIfEverythingIsOk(candidateVotes, checkedValues, notSupportedValue);
        }
    }

    private static String getCorrectPair(String candidatesPair, HashMap<String, Integer> candidateVotes) {
        var reversedKey = new StringBuilder(candidatesPair).reverse().toString();
        if (candidateVotes.get(candidatesPair) > candidateVotes.get(reversedKey)) {
            return candidatesPair;
        }
        return reversedKey;
    }

    private static List<List<String>> extractCandidates(List<List<String>> dataFromFile) {
        //todo validate size each row >= 3
        return Arrays.asList(
                Arrays.asList("A", "B", "C"),
                Arrays.asList("A", "C", "B"),
                Arrays.asList("C", "B", "A"),
                Arrays.asList("B", "C", "A"),
                Arrays.asList("B", "A", "C")
        );
    }

    private static List<Integer> extractVotes(List<List<String>> dataFromFile) {
        var resultList = new ArrayList<Integer>();
        for (int i = 0; i < dataFromFile.size(); i++) {
            resultList.add(Integer.valueOf(dataFromFile.get(i).get(0)));
        }
        return resultList;
    }
}
