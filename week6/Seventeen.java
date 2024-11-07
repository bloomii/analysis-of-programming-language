import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;
@SuppressWarnings("unchecked")
abstract class TFExercise {
    public String info() {
        return this.getClass().getName();
    }
}
@SuppressWarnings("unchecked")
class DataStorageManager extends TFExercise {
    private String data;

    public DataStorageManager(String pathToFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(sb.toString());
            data = matcher.replaceAll(" ").toLowerCase().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> words() {
        return Arrays.asList(data.split("\\s+"));
    }

    @Override
    public String info() {
        return super.info() + ": My major data structure is a " + data.getClass().getName();
    }
}
@SuppressWarnings("unchecked")
class StopWordManager extends TFExercise {
    private Set<String> stopWords = new HashSet<>();

    public StopWordManager() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("../stop_words.txt"));
            String[] words = br.readLine().split(",");
            Collections.addAll(stopWords, words);
            br.close();

            // Add single-letter words
            for (char c = 'a'; c <= 'z'; c++) {
                stopWords.add(String.valueOf(c));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    @Override
    public String info() {
        return super.info() + ": My major data structure is a " + stopWords.getClass().getName();
    }
}

class WordFrequencyManager extends TFExercise {
    private Map<String, Integer> wordFreqs = new HashMap<>();

    public void incrementCount(String word) {
        wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
    }

    public List<Map.Entry<String, Integer>> sorted() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFreqs.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }

    @Override
    public String info() {
        return super.info() + ": My major data structure is a " + wordFreqs.getClass().getName();
    }
}
@SuppressWarnings("unchecked")
class WordFrequencyController extends TFExercise {
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFreqManager;

    public WordFrequencyController(String pathToFile) {
        storageManager = new DataStorageManager(pathToFile);
        stopWordManager = new StopWordManager();
        wordFreqManager = new WordFrequencyManager();
    }
    private static void printClassInfo(Class<?> clazz) {
        // Print class name
        System.out.println("Class: " + clazz.getName());
        // Print fields
        System.out.println("    Fields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("        "+field.getName() + ": " + field.getType().getName());
        }
        // Print methods
        System.out.println("    Methods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.print("        "+method.getReturnType().getName()+"  "+method.getName()+"(");
            Class[] classes=method.getParameterTypes();
            for(int j=0;j< classes.length;j++){
                System.out.print(classes[j].getName());
                if(j!=classes.length-1){
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }

        // Print superclasses and interfaces
        System.out.println("    Superclass: " + clazz.getSuperclass().getName());
    }
    public void run() {
        // Use reflection to call methods dynamically
        try {
            Method wordsMethod = DataStorageManager.class.getMethod("words");
            List<String> words = (List<String>) wordsMethod.invoke(storageManager);

            for (String word : words) {
                Method isStopWordMethod = StopWordManager.class.getMethod("isStopWord", String.class);
                boolean isStopWord = (boolean) isStopWordMethod.invoke(stopWordManager, word);

                if (!isStopWord) {
                    Method incrementCountMethod = WordFrequencyManager.class.getMethod("incrementCount", String.class);
                    incrementCountMethod.invoke(wordFreqManager, word);
                }
            }

            Method sortedMethod = WordFrequencyManager.class.getMethod("sorted");
            List<Map.Entry<String, Integer>> wordFreqs = (List<Map.Entry<String, Integer>>) sortedMethod.invoke(wordFreqManager);

            // Print top 25 frequencies
            for (int i = 0; i < Math.min(25, wordFreqs.size()); i++) {
                System.out.println(wordFreqs.get(i).getKey() + " - " + wordFreqs.get(i).getValue());
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entry class name to inspect(DataStorageManager, StopWordManager, or WordFrequencyManager): ");
            String className = scanner.nextLine();
            try {
                // Dynamically load the class
                Class<?> clazz = Class.forName(className);
                printClassInfo(clazz);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found: " + className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
@SuppressWarnings("unchecked")
public class Seventeen {
    public static void main(String[] args) throws Exception {
//        WordFrequencyController controller = new WordFrequencyController("../pride-and-prejudice.txt");
        if (args.length < 1) {
            System.out.println("Please provide a file path.");
            return;
        }
        WordFrequencyController controller = new WordFrequencyController(args[0]);
        controller.run();
    }


}
