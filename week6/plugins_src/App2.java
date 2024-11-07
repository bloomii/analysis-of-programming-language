import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App2 extends Framework {
    public List<String> words(String filePath) {
        try {
            String fileText=new String(Files.readAllBytes(Paths.get(filePath)));
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(fileText);
            String[] words = matcher.replaceAll(" ").toLowerCase().trim().split("\\s+");
            List<String> stopWords=stopWords();
            return Arrays.stream(words).filter(word -> !stopWords.contains(word)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Map.Entry<String, Integer>> top25(List<String> wordList) {
        Map<String, Integer> frequencyMap = wordList.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        word -> 1,
                        Integer::sum
                ));

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(25)
                .collect(Collectors.toList());
    }
}
