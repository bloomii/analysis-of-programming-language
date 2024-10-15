import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class Nine {
    public static void main(String[] args) {
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        readFile(filePath,(BiConsumer)Nine::filterChars);
    }
    private static void readFile(Object f, Object func){
        String filePath=String.valueOf(f);
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
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
        biConsumerBiConsumer.accept(sb.toString(),Nine::normalize);
    }
    private static void filterChars(Object f,Object func){
        String dataString=String.valueOf(f);
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
        Pattern pattern = Pattern.compile("[\\W_]+");
        Matcher matcher = pattern.matcher(dataString);
        biConsumerBiConsumer.accept(matcher.replaceAll(" "),Nine::scan);
    }
    private static void normalize(Object f,Object func){
        String dataString=String.valueOf(f);
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
        biConsumerBiConsumer.accept(dataString.toLowerCase().trim(),Nine::removeStopWords);
    }
    private static void scan(Object f,Object func){
        String dataString=String.valueOf(f);
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
        biConsumerBiConsumer.accept(dataString.split("\\s+"),Nine::frequency);
    }
    private static void removeStopWords(Object f,Object func){
        String[] words=(String[]) f;
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
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
        biConsumerBiConsumer.accept(newWords,Nine::sort);
    }
    private static void frequency(Object f,Object func){
        List<String> words=(List<String>) f;
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
        Map<String,Integer> map=new HashMap<>();
        for(String w:words){
            if(map.containsKey(w)){
                map.put(w, map.get(w)+1);
            }else{
                map.put(w,1);
            }
        }
        biConsumerBiConsumer.accept(map,Nine::printAll);
    }
    private static void sort(Object f,Object func){
        Map<String,Integer> map=(Map<String,Integer>) f;
        BiConsumer<Object,BiConsumer> biConsumerBiConsumer=(BiConsumer<Object,BiConsumer>)func;
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        biConsumerBiConsumer.accept(list,Nine::noOption);
    }
    private static void printAll(Object f,Object func) {
        List<Map.Entry<String, Integer>> wordFreqs=(List<Map.Entry<String, Integer>>)f;
        for(int i=0;i<25&&i< wordFreqs.size();i++){
            System.out.println(wordFreqs.get(i).getKey() + "  -  " + wordFreqs.get(i).getValue());
        }
    }
    private static void noOption(Object o,Object func){
        return;
    }
}
