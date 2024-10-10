import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Six {
    public static void main(String[] args) {
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        printAll(sort(frequency(removeStopWords(scan(filterCharsAndNormalize(readFile(filePath)))))).subList(0,25));

    }
    private static String readFile(String filePath){
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
    private static String filterCharsAndNormalize(String dataString){
        Pattern pattern = Pattern.compile("[\\W_]+");
        Matcher matcher = pattern.matcher(dataString);
        return matcher.replaceAll(" ").toLowerCase().trim();
    }
    private static String[] scan(String dataString){
       return dataString.split("\\s+");
    }
    private static List<String> removeStopWords(String[] words){
        List<String> stopWords = new ArrayList<>();
        String filePath="../stop_words.txt";
        for(char c='a';c<='z';c++){
            stopWords.add(String.valueOf(c));
        }
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
        List<String> newWords=new ArrayList<>();
        for(String s:words){
            if(!stopWords.contains(s)){
                newWords.add(s);
            }
        }
        return newWords;
    }
    private static Map<String,Integer> frequency(List<String> words){
        Map<String,Integer> map=new HashMap<>();
        for(String w:words){
            if(map.containsKey(w)){
                map.put(w, map.get(w)+1);
            }else{
                map.put(w,1);
            }
        }
        return map;
    }
    private static List<Map.Entry<String, Integer>> sort(Map<String,Integer> map){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
    private static void printAll(List<Map.Entry<String, Integer>> wordFreqs) {
        if (wordFreqs.size() > 0) {
            System.out.println(wordFreqs.get(0).getKey() + "  -  " + wordFreqs.get(0).getValue());
            printAll(wordFreqs.subList(1, wordFreqs.size()));
        }
    }
}
