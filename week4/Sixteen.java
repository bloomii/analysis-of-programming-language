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

class EventManager{
    private Map<String,List<Consumer<Object[]>>> subscriptions;
    public EventManager(){
        subscriptions=new HashMap<>();
    }
    public void subscribe(String eventType,Consumer<Object[]> consumer){
        if(!subscriptions.containsKey(eventType)){
            subscriptions.put(eventType,new ArrayList<>());
        }
        subscriptions.get(eventType).add(consumer);
    }
    public void publish(Object[] event){
        String eventType=String.valueOf(event[0]);
        if(subscriptions.containsKey(eventType)){
            for(Consumer<Object[]> consumer:subscriptions.get(eventType)){
                consumer.accept(event);
            }
        }
    }
}
class DataStorage4Sixteen{
    private String data;
    private EventManager eventManager;
    public DataStorage4Sixteen(EventManager eventManager){
        this.eventManager=eventManager;
        this.eventManager.subscribe("load",this::load);
        this.eventManager.subscribe("start",this::produceWords);
    }
    private void load(Object[] event){
        String filePath=String.valueOf(event[1]);
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
    private void produceWords(Object[] event){
        for(String word:data.split(" ")){
            eventManager.publish(new Object[]{"word",word});
        }
        eventManager.publish(new Object[]{"eof",null});
    }
}
class StopWordFilter4Sixteen{
    private List<String> stopWords;
    private EventManager eventManager;
    public StopWordFilter4Sixteen(EventManager eventManager){
        this.eventManager=eventManager;
        this.eventManager.subscribe("load",this::load);
        this.eventManager.subscribe("word",this::isStopWord);
    }
    private void load(Object[] event){
        String ignore=String.valueOf(event[1]);
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
    private void isStopWord(Object[] event){
        String word=String.valueOf(event[1]);
        if(!stopWords.contains(word)){
            eventManager.publish(new Object[]{"valid_word",word});
        }

    }
}
class WordFrequencyCounter4Sixteen{
    private final Map<String,Integer> wordFrequency;
    private EventManager eventManager;
    public WordFrequencyCounter4Sixteen(EventManager eventManager){
        wordFrequency=new HashMap<>();
        this.eventManager=eventManager;
        this.eventManager.subscribe("valid_word",this::incrementCount);
        this.eventManager.subscribe("print",this::printFrequency);
    }
    private void incrementCount(Object[] event){
        String word=String.valueOf(event[1]);
        if(wordFrequency.containsKey(word)){
            wordFrequency.put(word, wordFrequency.get(word)+1);
        }else{
            wordFrequency.put(word,1);
        }
    }
    private void printFrequency(Object[] event){
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequency.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        for (int i=0;i<25&&i< list.size();i++) {
            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
        }
    }
}
// New class to count non-stop words containing 'z'
class WordStartWithZCounter{
    private int count = 0;
    private EventManager eventManager;
    public WordStartWithZCounter(EventManager eventManager) {
        this.eventManager=eventManager;
        this.eventManager.subscribe("valid_word",this::incrementCount);
        this.eventManager.subscribe("print",this::printZCount);
    }
    private void printZCount(Object[] event) {
        System.out.println("Number of non-stop words containing 'z': " + count);
    }
    public void incrementCount(Object[] event) {
        String word=String.valueOf(event[1]);
        if (word.contains("z")) {
            count++;
        }
    }
}
class WordFrequencyApplication{
    private EventManager eventManager;
    public WordFrequencyApplication(EventManager eventManager){
        this.eventManager=eventManager;
        this.eventManager.subscribe("run",this::run);
        this.eventManager.subscribe("eof",this::stop);
    }
    private void run(Object[] event){
        String filePath=String.valueOf(event[1]);
        eventManager.publish(new Object[]{"load",filePath});
        eventManager.publish(new Object[]{"start",null});
    }
    private void stop(Object[] event){
        eventManager.publish(new Object[]{"print",null});
    }
}
public class Sixteen {
    public static void main(String[] args){

        EventManager eventManager=new EventManager();
        new DataStorage4Sixteen(eventManager);
        new StopWordFilter4Sixteen(eventManager);
        new WordFrequencyCounter4Sixteen(eventManager);
        new WordFrequencyApplication(eventManager);
        new WordStartWithZCounter(eventManager);
//        eventManager.publish(new Object[]{"run",args[0]});
        eventManager.publish(new Object[]{"run","../pride-and-prejudice.txt"});
    }
}
