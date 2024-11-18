import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwentyEight {
    public static Stream<String> lines(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(line->line.toLowerCase().trim()+"\n");

    }
    public static Stream<String> allWords(String filePath) throws IOException {
        return Arrays.stream(
                        lines(filePath)
                          .flatMapToInt(String::chars)  // Convert each line to a stream of integers (Unicode values of characters)
                          .mapToObj(c -> String.valueOf((char) c)) // Convert each integer back to a character (String)
                            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)  // Collect characters into words
                            .toString()
                            .split("[^a-z]")  // Split the string by non-word characters
                )
                .filter(word -> !word.isEmpty());  // Filter out empty words
    }
    public static Stream<String> nonStopWords(String filePath) throws IOException {
        Set<String> stopWords=new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get("../stop_words.txt"));
        for (String line : lines) {
            String[] words = line.trim().split("[,\\s]+");
            stopWords.addAll(Arrays.asList(words));
        }
        for(char c='a';c<='z';c++){
            stopWords.add(String.valueOf(c));
        }
        return allWords(filePath)
                .filter(word -> !stopWords.contains(word));  // Filter out stopWords
    }

    public static void countAndSort(String filePath) throws IOException {
        Map<String, Long> wordCounts = nonStopWords(filePath)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));  // Count occurrences

        // Sort the words by frequency in descending order and print top 25
        wordCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))  // Sort by frequency
                .limit(25)  // Get the top 25 most frequent words
                .forEach(entry -> System.out.println(entry.getKey() + "  -  " + entry.getValue()));
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequencyCounter <inputFile>");
            return;
        }
        String filePath=args[0];
//        String filePath="../pride-and-prejudice.txt";

        countAndSort(filePath);
    }
}
