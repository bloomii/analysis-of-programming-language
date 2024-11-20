import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
@SuppressWarnings("unchecked")
class ActiveWFObject extends Thread{
    String name;
    private LinkedBlockingDeque<List<Object>> queue;
    public boolean stopMe;
    public ActiveWFObject(){
        super();
        queue=new LinkedBlockingDeque<>();
        stopMe=false;
        start();
    }
    public void run(){
        while(!stopMe){
            try {
                List<Object> message = queue.take();
                dispatch(message);
                if ("die".equals(message.get(0))) {
                    stopMe = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void dispatch(List<Object> message){

    }
    public LinkedBlockingDeque<List<Object>> getQueue(){
        return queue;
    }

}
class DataStorageManager extends ActiveWFObject {
    private String data = "";
    private StopWordManager stopWordManager;

    @Override
    public void dispatch(List<Object> message) {
        String command = (String) message.get(0);
        if ("init".equals(command)) {
            init((String) message.get(1), (StopWordManager) message.get(2));
        } else if ("send_word_freqs".equals(command)) {
            processWords((WordFrequencyController) message.get(1));
        } else {
            TwentyNine.send(stopWordManager, message);
        }
    }

    public void init(String pathToFile, StopWordManager stopWordManager) {
        this.stopWordManager = stopWordManager;
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            data = sb.toString().toLowerCase().replaceAll("[\\W_]+", " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processWords(WordFrequencyController recipient) {
        String[] words = data.split("\\s+");
        for (String word : words) {
            TwentyNine.send(stopWordManager, Arrays.asList("filter", word));
        }
        TwentyNine.send(stopWordManager, Arrays.asList("top25", recipient));
    }
}

class StopWordManager extends ActiveWFObject {
    private Set<String> stopWords;
    private WordFrequencyManager wordFreqsManager;

    @Override
    public void dispatch(List<Object> message) {
        String command = (String) message.get(0);
        if ("init".equals(command)) {
            init((WordFrequencyManager) message.get(1));
        } else if ("filter".equals(command)) {
            filter((String) message.get(1));
        } else {
            TwentyNine.send(wordFreqsManager, message);
        }
    }

    public void init(WordFrequencyManager wordFreqsManager) {
        this.stopWords = new HashSet<>();
        this.wordFreqsManager = wordFreqsManager;
        try (BufferedReader reader = new BufferedReader(new FileReader("../stop_words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.addAll(Arrays.asList(line.split(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(char c='a';c<='z';c++){
            stopWords.add(String.valueOf(c));
        }
    }

    public void filter(String word) {
        if (!stopWords.contains(word)) {
            TwentyNine.send(wordFreqsManager, Arrays.asList("word", word));
        }
    }
}

class WordFrequencyManager extends ActiveWFObject {
    private Map<String, Integer> wordFreqs;
    public WordFrequencyManager(){
        wordFreqs= new HashMap<>();
    }
    @Override
    public void dispatch(List<Object> message) {
        String command = (String) message.get(0);
        if ("word".equals(command)) {
            incrementCount((String) message.get(1));
        } else if ("top25".equals(command)) {
            top25((WordFrequencyController) message.get(1));
        }
    }

    public void incrementCount(String word) {
        wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
    }

    public void top25(WordFrequencyController recipient) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFreqs.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        TwentyNine.send(recipient, Arrays.asList("top25", sortedList));
    }
}
@SuppressWarnings("unchecked")
class WordFrequencyController extends ActiveWFObject {
    private DataStorageManager storageManager;

    @Override
    public void dispatch(List<Object> message) {
        String command = (String) message.get(0);
        if ("run".equals(command)) {
            run((DataStorageManager) message.get(1));
        } else if ("top25".equals(command)) {
            display((List<Map.Entry<String, Integer>>) message.get(1));
        } else {
            throw new IllegalArgumentException("Message not understood: " + message.get(0));
        }
    }

    public void run(DataStorageManager storageManager) {
        this.storageManager=storageManager;
        TwentyNine.send(this.storageManager, Arrays.asList("send_word_freqs", this));
    }

    public void display(List<Map.Entry<String, Integer>> wordFreqs) {
        for (Map.Entry<String, Integer> entry : wordFreqs.subList(0, Math.min(25, wordFreqs.size()))) {
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
        TwentyNine.send(storageManager, Arrays.asList("die"));
        stopMe = true;
    }
}
public class TwentyNine {
    public static void send(ActiveWFObject receiver, List<Object> message) {
        receiver.getQueue().offer(message);
    }
    public static void main(String[] args) {
        WordFrequencyManager wordFreqManager = new WordFrequencyManager();
        StopWordManager stopWordManager = new StopWordManager();
        WordFrequencyController wfController = new WordFrequencyController();
        DataStorageManager storageManager = new DataStorageManager();
        send(stopWordManager, Arrays.asList("init", wordFreqManager));
        send(storageManager, Arrays.asList("init", args[0], stopWordManager));
//        send(storageManager, Arrays.asList("init", "../pride-and-prejudice.txt", stopWordManager));
        send(wfController, Arrays.asList("run", storageManager));
        try {
            wordFreqManager.join();
            stopWordManager.join();
            storageManager.join();
            wfController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
