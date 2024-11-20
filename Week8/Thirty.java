import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class Thirty {
    // Data spaces
    static BlockingQueue<String> wordSpace = new LinkedBlockingQueue<>();
    static BlockingQueue<Map<String, Integer>> freqSpace = new LinkedBlockingQueue<>();

    // StopWords set
    static Set<String> stopWords = new HashSet<>();

    // Load stopWords
    private static void loadStopWords(String stopWordsFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.addAll(Arrays.asList(line.split(",")));
            }
        }
    }

    // Worker function to process words and count their frequencies
    private static void processWords() {
        Map<String, Integer> wordFreqs = new HashMap<>();
        while (true) {
            try {
                String word = wordSpace.poll(1, TimeUnit.SECONDS);
                if (word == null) {
                    break;
                }
                if (!stopWords.contains(word)) {
                    wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            freqSpace.put(wordFreqs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Main method
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 1) {
            System.err.println("Usage: java WordFrequencyCounter <input-file>");
            System.exit(1);
        }

        String inputFile = args[0];
//        String inputFile = "../pride-and-prejudice.txt";
        loadStopWords("../stop_words.txt");

        // Read input file and put words into the word space
        String content = new String(Files.readAllBytes(Paths.get(inputFile)), StandardCharsets.UTF_8);
        Pattern wordPattern = Pattern.compile("[a-z]{2,}");
        Matcher matcher = wordPattern.matcher(content.toLowerCase());
        while (matcher.find()) {
            wordSpace.put(matcher.group());
        }

        // Create and start worker threads
        int numWorkers = 5;
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            Thread worker = new Thread(Thirty::processWords);
            workers.add(worker);
            worker.start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
        Map<String, Integer> wordFreqs = new HashMap<>();
        while (!freqSpace.isEmpty()) {
            Map<String, Integer> freqs = freqSpace.take();
            for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                wordFreqs.put(entry.getKey(),
                        wordFreqs.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        // Sort and print the top 25 words
        wordFreqs.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + "  -  " + entry.getValue()));
    }
}
