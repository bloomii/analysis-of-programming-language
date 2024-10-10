import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Seven {
    public static void main(String[] args) {
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        Set<String> stopWords = loadStopWords("../stop_words.txt");
        Map<String, Integer> wordCount = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            Pattern pattern = Pattern.compile("[a-z]{2,}");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.toLowerCase());
                while (matcher.find()) {
                    String word = matcher.group();
                    if (!stopWords.contains(word)) {
                        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    }
                }
            }
            reader.close();

            // Get the top 25 words
            List<Map.Entry<String, Integer>> wordList = new ArrayList<>(wordCount.entrySet());
            wordList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Print the top 25 words
            for (int i = 0; i < Math.min(25, wordList.size()); i++) {
                Map.Entry<String, Integer> entry = wordList.get(i);
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
    private static Set<String> loadStopWords(String filePath) {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(",");
                Collections.addAll(stopWords, words);
            }
        } catch (IOException e) {
            System.err.println("Error reading stop words file: " + e.getMessage());
        }
        return stopWords;
    }
}
