import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

class Pair{
    public String word;
    public int count;
    public Pair(String w,int c){
        word=w;
        count=c;
    }
}
@SuppressWarnings("unchecked")
public class ThirtyTwo {
    public static void main(String[] args) throws IOException {
//        String data = readFile("../pride-and-prejudice.txt");
        String data = readFile(args[0]);
        Stream<String> partitions = partition(data, 200);
        List<List<Pair>> splits = partitions
                .map(ThirtyTwo::splitWords)
                .collect(Collectors.toList());
        Map<String, List<Pair>> regrouped = regroup(splits);
        List<Map.Entry<String, Integer>> wordFreqs = regrouped.entrySet().stream()
                .flatMap(ThirtyTwo::countWords)
                .collect(Collectors.toList());
        sort(wordFreqs);
        for (Map.Entry<String, Integer> entry : wordFreqs.subList(0, Math.min(25, wordFreqs.size()))) {
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
    }
    public static Stream<String> partition(String dataStr, int nLines) {
        List<String> lines = Arrays.asList(dataStr.split("\n"));
        Stream.Builder<String> partitions = Stream.builder();
        for (int i = 0; i < lines.size(); i += nLines) {
            partitions.add(String.join("\n", lines.subList(i, Math.min(i + nLines, lines.size()))));
        }
        return partitions.build();
    }
    public static List<Pair> splitWords(String dataStr) {
        List<String> words = removeStopWords(scan(dataStr));
        List<Pair> wordCountPairs = new ArrayList<>();
        for (String word : words) {
            wordCountPairs.add(new Pair(word, 1));
        }
        return wordCountPairs;
    }
    public static List<String> scan(String strData) {
        Pattern pattern = Pattern.compile("[\\W_]+");
        String cleanedStr = pattern.matcher(strData).replaceAll(" ").toLowerCase();
        return Arrays.asList(cleanedStr.split(" ")).stream().filter(word->!word.isEmpty()).collect(Collectors.toList());
    }
    public static List<String> removeStopWords(List<String> wordList) {
        Set<String> stopWords = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("../stop_words.txt"));
            String stopWordsData = reader.readLine();
            reader.close();
            stopWords.addAll(Arrays.asList(stopWordsData.split(",")));
            for(char c='a';c<='z';c++){
                stopWords.add(String.valueOf(c));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList.stream()
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.toList());
    }
    private static String getGroup(char c){
        if(c>='a'&&c<='e'){
            return "a-e";
        } else if (c>='f'&&c<='j') {
            return "f-j";
        } else if (c>='k' && c<='o') {
            return "k-o";
        } else if (c>='p' && c<='t') {
            return "p-t";
        }else {
            return "u-z";
        }
    }
    // Regroup a list of lists of pairs by word
    public static Map<String, List<Pair>> regroup(List<List<Pair>> pairsList) {
        Map<String, List<Pair>> mapping = new HashMap<>();
        for (List<Pair> pairs : pairsList) {
            for (Pair pair : pairs) {
                String groupName=getGroup(pair.word.charAt(0));
                if(!mapping.containsKey(groupName)){
                    mapping.put(groupName,new ArrayList<>());
                }
                mapping.get(groupName).add(pair);
            }
        }

        return mapping;
    }

    // Count the frequency of a word from its list of pairs (word, 1)
    public static Stream<Map.Entry<String, Integer>> countWords(Map.Entry<String, List<Pair>> entry) {
        Map<String,Integer> map=new HashMap<>();
        for(Pair pair: entry.getValue()){
            map.put(pair.word, map.getOrDefault(pair.word,0)+1);
        }
        return map.entrySet().stream();
    }

    // Read file contents into a string
    public static String readFile(String pathToFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
    public static void sort(List<Map.Entry<String,Integer>> unSortedList){
        unSortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
    }
}
