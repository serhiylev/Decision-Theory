package util;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    @SneakyThrows
    public static List<List<Integer>> getIntegerInputDataFromFile(String path) {
        return Files.lines(Path.of(path))
                .map(line -> line.split(" "))
                .map(array -> Arrays.stream(array.clone())
                        .map(Integer::valueOf)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static List<List<String>> getStringInputDataFromFile(String path) {
        return Files.lines(Path.of(path))
                .map(line -> line.split(" "))
                .map(array -> Arrays.stream(array.clone())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static <T> T extractDataFromJsonInputFile(String inputFilePath, Class<T> mappedClass) {
        var gson = new Gson();
        var pathToTheInputFile = Path.of(inputFilePath);
        var reader = Files.newBufferedReader(pathToTheInputFile);

        return gson.fromJson(reader, mappedClass);
    }
}
