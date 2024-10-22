import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordFrequencyFramework{
    private List<Consumer<String>> loadEventHandlers;
    private List<Runnable> doWorkEventHandlers;
    private List<Runnable> endEventHandlers;
    public WordFrequencyFramework(){
        loadEventHandlers=new ArrayList<>();
        doWorkEventHandlers=new ArrayList<>();
        endEventHandlers=new ArrayList<>();
    }
    public void register4LoadEvent(Consumer<String> handler){
        loadEventHandlers.add(handler);
    }
    public void register4DoWorkEvent(Runnable handler){
        doWorkEventHandlers.add(handler);
    }
    public void register4EndEvent(Runnable handler){
        endEventHandlers.add(handler);
    }
    public void run(String filePath){
        for(Consumer<String> consumer:loadEventHandlers){
            consumer.accept(filePath);
        }
        for(Runnable runnable:doWorkEventHandlers){
            runnable.run();
        }
        for(Runnable runnable:endEventHandlers){
            runnable.run();
        }
    }
}
class DataStorage{
    private String data;
    private final StopWordFilter stopWordFilter;
    private final List<Consumer<String>> wordEventHandler;

    public DataStorage(WordFrequencyFramework wordFrequencyFramework,StopWordFilter stopWordFilter){
        wordEventHandler=new ArrayList<>();
        this.stopWordFilter=stopWordFilter;
        wordFrequencyFramework.register4LoadEvent(this::load);
        wordFrequencyFramework.register4DoWorkEvent(this::produceWords);
    }
    private void load(String filePath){
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
            data=sb.toString();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(String.valueOf(data));
            data=matcher.replaceAll(" ").toLowerCase().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void produceWords(){
        for(String word:data.split(" ")){
            if(!stopWordFilter.isStopWord(word)){
                for(Consumer<String> consumer:wordEventHandler){
                    consumer.accept(word);
                }
            }
        }
    }
    public void register4WorkEvent(Consumer<String> handler){
        wordEventHandler.add(handler);
    }
}
class StopWordFilter{
    private List<String> stopWords;
    public StopWordFilter(WordFrequencyFramework wordFrequencyFramework){
        wordFrequencyFramework.register4LoadEvent(this::load);
    }
    private void load(String ignore){
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
    }
    public boolean isStopWord(String word){
        return stopWords.contains(word);
    }
}
class WordFrequencyCounter{
    private final Map<String,Integer> wordFrequency;
    public WordFrequencyCounter(WordFrequencyFramework wordFrequencyFramework,DataStorage dataStorage){
        wordFrequency=new HashMap<>();
        dataStorage.register4WorkEvent(this::incrementCount);
        wordFrequencyFramework.register4EndEvent(this::printFrequency);
    }
    private void incrementCount(String word){
        if(wordFrequency.containsKey(word)){
            wordFrequency.put(word, wordFrequency.get(word)+1);
        }else{
            wordFrequency.put(word,1);
        }
    }
    private void printFrequency(){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequency.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        for (int i=0;i<25&&i< list.size();i++) {
            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
        }
    }
}
public class Fifteen {
    public static void main(String[] args){
        WordFrequencyFramework wordFrequencyFramework=new WordFrequencyFramework();
        StopWordFilter stopWordFilter=new StopWordFilter(wordFrequencyFramework);
        DataStorage dataStorage=new DataStorage(wordFrequencyFramework,stopWordFilter);
        WordFrequencyCounter wordFrequencyCounter=new WordFrequencyCounter(wordFrequencyFramework,dataStorage);
//        wordFrequencyFramework.run("../pride-and-prejudice.txt");
        wordFrequencyFramework.run(args[0]);
    }
}
