import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataStorageManager {
    private Object data;
    public Object dispatch(String[] message) {
        if(message[0].equals("init")) {
            return init(String.valueOf(message[1]));
        }else if(message[0].equals("words")){
            return words();
        }else{
            throw new RuntimeException("Message not understood "+message[0]);
        }
    }
    private Object init(String fileName) {
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
            data=sb.toString();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(String.valueOf(data));
            data=matcher.replaceAll(" ").toLowerCase().trim();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String[] words(){
        String dataStr = String.valueOf(data);
        return dataStr.split(" ");
    }
}
class StopWordManager{
    private List<String> stopWords;
    public Object dispatch(String[] message){
        if(message[0].equals("init")) {
            return init();
        } else if (message[0].equals("is_stop_word")) {
            return isStopWords(message[1]);
        }else{
            throw new RuntimeException("Message not understood "+message[0]);
        }
    }
    private Object init(){
        stopWords=new ArrayList<>();
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
        return null;
    }
    private Boolean isStopWords(String word){
        return stopWords.contains(word);
    }
}
class WordFrequencyManager{
    private final Map<String,Integer> wordFrequency;
    public WordFrequencyManager(){
        wordFrequency=new HashMap<>();
    }
    public Object dispatch(String[] message){
        if(message[0].equals("increment_count")) {
            return incrementCount(String.valueOf(message[1]));
        } else if (message[0].equals("sorted")) {
            return sorted();
        }else{
            throw new RuntimeException("Message not understood "+message[0]);
        }
    }
    private Object incrementCount(String word){
        if(wordFrequency.containsKey(word)){
            wordFrequency.put(word, wordFrequency.get(word)+1);
        }else{
            wordFrequency.put(word,1);
        }
        return null;
    }
    private Object sorted(){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequency.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
}
@SuppressWarnings("unchecked")
class WordFrequencyController{
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFrequencyManager;
    public Object dispatch(String[] message){
        if(message[0].equals("init")) {
            return init(message[1]);
        } else if (message[0].equals("run")) {
            return run();
        }else{
            throw new RuntimeException("Message not understood "+message[0]);
        }
    }
    private Object init(String filePath){
        storageManager=new DataStorageManager();
        stopWordManager=new StopWordManager();
        wordFrequencyManager=new WordFrequencyManager();
        storageManager.dispatch(new String[]{"init",filePath});
        stopWordManager.dispatch(new String[]{"init"});
        return null;
    }

    private Object run(){
        for(String word:(String[])(storageManager.dispatch(new String[]{"words"}))){
            if(!(Boolean) stopWordManager.dispatch(new String[]{"is_stop_word",word})){
                wordFrequencyManager.dispatch(new String[]{"increment_count",word});
            }
        }
        List<Map.Entry<String, Integer>> list= (List<Map.Entry<String, Integer>>) wordFrequencyManager.dispatch(new String[]{"sorted"});
        for (int i=0;i<25&&i< list.size();i++) {
            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
        }
        return null;
    }
}
public class Twelve {
    public static void main(String[] args){
        WordFrequencyController wfController = new WordFrequencyController();
       // wfController.dispatch(new String[]{"init", "../pride-and-prejudice.txt"});
        wfController.dispatch(new String[]{"init", args[0]});
        wfController.dispatch(new String[]{"run"});
    }
}
