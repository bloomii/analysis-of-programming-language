import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class TwentyOne {
    public static void main(String[] args) {
        String filePath =args.length>=1 ? args[0]:"../pride-and-prejudice.txt";
        printAll(sort(frequency(removeStopWords(scan(filterCharsAndNormalize(readFile(filePath)))))).subList(0,25));

    }
    private static String readFile(String filePath){
        if(filePath==null||!(filePath instanceof String)||filePath.equals("")){
            return "";
        }
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
        } catch (IOException e) {
            return "";
        }
        return sb.toString();
    }
    private static String filterCharsAndNormalize(String dataString){
        if(dataString==null||!(dataString instanceof String)||dataString.equals("")){
            return "";
        }
        Pattern pattern = Pattern.compile("[\\W_]+");
        Matcher matcher = pattern.matcher(dataString);
        return matcher.replaceAll(" ").toLowerCase().trim();
    }
    private static String[] scan(String dataString){
        if(dataString==null||!(dataString instanceof String)||dataString.equals("")){
            return new String[0];
        }
        return dataString.split("\\s+");
    }
    private static List<String> removeStopWords(String[] words){
        if(words==null||!(words instanceof String[])||words.length==0){
            return new ArrayList<>();
        }
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
            return Arrays.stream(words).toList();
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
        if(words==null||!(words instanceof List) || words.size() == 0){
            return new HashMap<>();
        }
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
        if(map==null||!(map instanceof Map)||map.size()==0){
            return new ArrayList<>();
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
    private static void printAll(List<Map.Entry<String, Integer>> wordFreqs) {
        if(wordFreqs==null||!(wordFreqs instanceof List)){
            return;
        }
        if (wordFreqs.size() > 0) {
            System.out.println(wordFreqs.get(0).getKey() + "  -  " + wordFreqs.get(0).getValue());
            printAll(wordFreqs.subList(1, wordFreqs.size()));
        }
    }
}
