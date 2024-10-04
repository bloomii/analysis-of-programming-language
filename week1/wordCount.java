import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class wordCount {
    public static void main(String[] args) {
        Set<String> stopWords = loadStopWords("../stop_words.txt");

       String filePath = args[0];
        // String filePath = "../pride-and-prejudice.txt";
        Map<String,Integer> map = countWords(filePath, stopWords);

        printTop25(map);
    }
    private static void printTop25(Map<String, Integer> map){
        Map<String, Integer> sortedMap = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String k1, String k2) {
                        return map.get(k2) - map.get(k1);
                    }
                }
        );
        sortedMap.putAll(map);
        int top=0;
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            top++;
            if(top>=25) break;
        }
    }
    private static Set<String> loadStopWords(String filePath) {
        Set<String> stopWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] l=line.trim().split(",");
                for(String s:l) {
                    stopWords.add(s.trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading stop words file: " + e.getMessage());
        }
        return stopWords;
    }
    private static Map<String, Integer> countWords(String filePath, Set<String> stopWords) {
        Map<String, Integer> wordCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("[^a-zA-Z']");
                for (String word : words) {
                    word = word.toLowerCase();
                    if(word.startsWith("'")) word=word.substring(1);
                    if(word.endsWith("'s")) word=word.substring(0, word.length()-2);
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return wordCountMap;
    }
}
