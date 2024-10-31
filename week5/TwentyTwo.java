import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class TwentyTwo {
    public static void main(String[] args) {
        try {
            if(args.length<1){
                throw new RuntimeException("You idiot! I need an input file!");
            }
            String filePath  =  args[0];
            printAll(sort(frequency(removeStopWords(scan(filterCharsAndNormalize(readFile(filePath)))))).subList(0, 25));
        }catch (Exception e){
            System.out.println("Something wrong: "+e.getMessage());
            e.printStackTrace();
        }
    }
    private static String readFile(String filePath) throws IOException {
        if(filePath==null){
            throw new RuntimeException("I need a non-null string");
        }
        if(!(filePath instanceof String)){
            throw new RuntimeException("OMG! This is not a String!");
        }
        if(filePath.equals("")){
            throw new RuntimeException("I need a non-empty string!");
        }
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
        } catch (IOException e) {
            throw e;
        }
        return sb.toString();
    }
    private static String filterCharsAndNormalize(String dataString){
        if(dataString==null){
            throw new RuntimeException("I need a non-null string");
        }
        if(!(dataString instanceof String)){
            throw new RuntimeException("OMG! This is not a String!");
        }
        if(dataString.equals("")){
            throw new RuntimeException("I need a non-empty string!");
        }
        Pattern pattern = Pattern.compile("[\\W_]+");
        Matcher matcher = pattern.matcher(dataString);
        return matcher.replaceAll(" ").toLowerCase().trim();
    }
    private static String[] scan(String dataString){
        if(dataString==null){
            throw new RuntimeException("I need a non-null string");
        }
        if(!(dataString instanceof String)){
            throw new RuntimeException("OMG! This is not a String!");
        }
        if(dataString.equals("")){
            throw new RuntimeException("I need a non-empty string!");
        }
        return dataString.split("\\s+");
    }
    private static List<String> removeStopWords(String[] words) throws IOException {
        if(words==null){
            throw new RuntimeException("I need a non-null string array");
        }
        if(!(words instanceof String[])){
            throw new RuntimeException("OMG! This is not a String array!");
        }
        if(words.length==0){
            throw new RuntimeException("I need a non-empty string array!");
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
            throw e;
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
        if(words==null){
            throw new RuntimeException("I need a non-null List");
        }
        if(!(words instanceof List)){
            throw new RuntimeException("OMG! This is not a List!");
        }
        if(words.size()==0){
            throw new RuntimeException("I need a non-empty List!");
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
        if(map==null){
            throw new RuntimeException("I need a non-null HashMap");
        }
        if(!(map instanceof Map)){
            throw new RuntimeException("OMG! This is not a Map!");
        }
        if(map.size()==0){
            throw new RuntimeException("I need a non-empty HashMap!");
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
    private static void printAll(List<Map.Entry<String, Integer>> wordFreqs) {
        if(wordFreqs==null){
            throw new RuntimeException("I need a non-null List");
        }
        if(!(wordFreqs instanceof List)){
            throw new RuntimeException("OMG! This is not a List!");
        }
        if (wordFreqs.size() > 0) {
            System.out.println(wordFreqs.get(0).getKey() + "  -  " + wordFreqs.get(0).getValue());
            printAll(wordFreqs.subList(1, wordFreqs.size()));
        }
    }
}
