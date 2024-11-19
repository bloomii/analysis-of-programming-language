import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwentyEight {
    public static Stream<String> lines(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(line->line.toLowerCase());

    }
    public static Stream<String> allWords(String filePath) throws IOException {
        return
            lines(filePath)
                .flatMap(line -> {
                    Pattern WORD_PATTERN = Pattern.compile("[a-z]{2,}");
                    Matcher matcher = WORD_PATTERN.matcher(line);
                    Stream.Builder<String> wordStreamBuilder = Stream.builder();
                    while (matcher.find()) {
                        wordStreamBuilder.add(matcher.group().trim());
                    }
                    return wordStreamBuilder.build();
                })
                .filter(word -> !word.isEmpty());  // Filter out empty words
    }
    public static Stream<String> nonStopWords(String filePath) throws IOException {
        Set<String> stopWords=new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get("../stop_words.txt"));
        for (String line : lines) {
            String[] words = line.trim().split(",");
            stopWords.addAll(Arrays.asList(words));
        }
        for(char c='a';c<='z';c++){
            stopWords.add(String.valueOf(c));
        }
        return allWords(filePath)
                .filter(word -> !stopWords.contains(word));  // Filter out stopWords
    }

    public static Stream<List<Map.Entry<String,Integer>>> countAndSort(String filePath) throws IOException {
        Map<String,Integer> wordFrequency=new HashMap<>();
        int[] i={1};
        Stream.Builder<List<Map.Entry<String,Integer>>> list = Stream.builder();
        nonStopWords(filePath).forEach((w)->{
            wordFrequency.put(w, wordFrequency.getOrDefault(w,0)+1);
            if(i[0]%5000==0){
                list.add(wordFrequency.entrySet().stream()
                        .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())).toList());
            }
            i[0]++;
        });
        return list.build();
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequencyCounter <inputFile>");
            return;
        }
        String filePath=args[0];
//        String filePath="../pride-and-prejudice.txt";

        countAndSort(filePath).forEach(s->{
            System.out.println("-----------------------------------------");
            for(int i=0;i<25&&i<s.size();i++){
                System.out.println(s.get(i).getKey() + "  -  " + s.get(i).getValue());
            }
        });
    }
}
