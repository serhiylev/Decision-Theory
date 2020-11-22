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
        System.out.println("\nBorda Method");

        var pointsPerCandidate = new HashMap<String, Integer>();

        var votes = extractVotes(dataFromFile);
        var candidateOrders = extractCandidates(dataFromFile);

        for (int i = 0; i < candidateOrders.size(); i++) {
            var currentCandidatesOrder = candidateOrders.get(i);
            var currentVotes = votes.get(i);
            for (int j = 0; j < currentCandidatesOrder.size(); j++) {
                var currentPointsCoefficient = currentCandidatesOrder.size() - (j + 1);
                var points = currentVotes * currentPointsCoefficient;

                pointsPerCandidate.compute(currentCandidatesOrder.get(j), (key, value) -> value == null ? points : value + points);
            }
        }

        System.out.println(pointsPerCandidate);
        var result = pointsPerCandidate.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("There is no max value"));

        System.out.println("Winner = " + result.getKey());
    }

    private static void calculateWinnerViaCondorcetMethod(List<List<String>> dataFromFile) {
        System.out.println("Condorcet method");

        var votes = extractVotes(dataFromFile);
        var candidateOrders = extractCandidates(dataFromFile);

        HashMap<String, Integer> candidateVotes = fillCandidateVotesMap(votes, candidateOrders);

        System.out.println(candidateVotes);

        var maxEntry = candidateVotes.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("There is no max value"));


        var winner = findWinner(candidateVotes, maxEntry.getKey());

        System.out.println("Winner = " + winner.toCharArray()[0]);
    }

    private static HashMap<String, Integer> fillCandidateVotesMap(List<Integer> votes, List<List<String>> candidateOrders) {
        var candidateVotes = new HashMap<String, Integer>();

        for (int i = 0; i < candidateOrders.size(); i++) {
            var candidatesOrder = candidateOrders.get(i);
            for (int j = 0; j < candidatesOrder.size() - 1; j++) {
                var currentVotes = votes.get(i);
                for (int k = j + 1; k < candidatesOrder.size(); k++) {
                    var currentKey = candidatesOrder.get(j) + candidatesOrder.get(k);
                    candidateVotes.compute(currentKey, (key, value) -> value == null ? currentVotes : value + currentVotes);
                }
            }
        }
        return candidateVotes;
    }

    private static String findWinner(HashMap<String, Integer> candidateVotes, String currentEntry) {
        var valuesToCheck = new ArrayList<String>();

        for (String key : candidateVotes.keySet()) {
            var firstLetterOfCurrentEntry = String.valueOf(currentEntry.toCharArray()[0]);
            if (key.startsWith(firstLetterOfCurrentEntry) && !key.equals(currentEntry)) {
                valuesToCheck.add(key);
            }
        }

        var currentEntryIsTheBiggest = valuesToCheck.stream()
                .allMatch(value -> value.equals(getBiggerPair(value, candidateVotes)));

        if (currentEntryIsTheBiggest) {
            return currentEntry;
        } else {
            var notSupportedValue = "";
            for (String value : valuesToCheck) {
                if (!value.equals(getBiggerPair(value, candidateVotes))) {
                    notSupportedValue = getBiggerPair(value, candidateVotes);
                }
            }
            return findWinner(candidateVotes, notSupportedValue);
        }
    }

    private static String getBiggerPair(String candidatesPair, HashMap<String, Integer> candidateVotes) {
        var reversedKey = new StringBuilder(candidatesPair).reverse().toString();

        if (candidateVotes.get(candidatesPair) > candidateVotes.get(reversedKey)) {
            return candidatesPair;
        }
        return reversedKey;
    }

    private static List<List<String>> extractCandidates(List<List<String>> dataFromFile) {
        var result = new ArrayList<List<String>>();

        for (List<String> strings : dataFromFile) {
            var tempList = new ArrayList<String>();
            for (int j = 1; j < strings.size(); j++) {
                tempList.add(strings.get(j));
            }
            result.add(tempList);
        }
        return result;
    }

    private static List<Integer> extractVotes(List<List<String>> dataFromFile) {
        var resultList = new ArrayList<Integer>();

        for (List<String> strings : dataFromFile) {
            resultList.add(Integer.valueOf(strings.get(0)));
        }
        return resultList;
    }
}
