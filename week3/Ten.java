import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("unchecked")
class TFTheOne{
    Object v;
    public TFTheOne(Object v){
        this.v=v;
    }
    public TFTheOne bind(Function func){
        v=func.apply(v);
        return this;
    }
    public void printMe(){
        List<Map.Entry<String, Integer>> wordFreqs=(List<Map.Entry<String, Integer>>)v;
        for(Map.Entry<String, Integer> entry:wordFreqs){
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
    }
}
@SuppressWarnings("unchecked")
public class Ten {

    public static void main(String[] args){
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        new TFTheOne(filePath)
                .bind(Ten::readFile)
                .bind(Ten::filterChars)
                .bind(Ten::normalize)
                .bind(Ten::scan)
                .bind(Ten::removeStopWords)
                .bind(Ten::frequency)
                .bind(Ten::sort)
                .bind(Ten::top25Frequency)
                .printMe();
    }
    private static String readFile(Object o){
        String filePath=String.valueOf(o);
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
    private static String filterChars(Object o){
        String dataString=String.valueOf(o);
        Pattern pattern = Pattern.compile("[\\W_]+");
        Matcher matcher = pattern.matcher(dataString);
        return matcher.replaceAll(" ");
    }
    private static String normalize(Object o){
        String dataString=String.valueOf(o);
        return dataString.trim().toLowerCase();
    }
    private static String[] scan(Object o){
        String dataString=String.valueOf(o);
        return dataString.split("\\s+");
    }
    private static List<String> removeStopWords(Object o){
        String[] words=(String[]) o;
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
    private static Map<String,Integer> frequency(Object o){
        List<String> words=(List<String>) o;
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
    private static List<Map.Entry<String, Integer>> sort(Object o){
        Map<String,Integer> map=(Map<String,Integer>)o;
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
    private static List<Map.Entry<String, Integer>> top25Frequency(Object o) {
        List<Map.Entry<String, Integer>> wordFreqs=(List<Map.Entry<String, Integer>>)o;
        return wordFreqs.subList(0,Math.min(25, wordFreqs.size()));
    }
}
