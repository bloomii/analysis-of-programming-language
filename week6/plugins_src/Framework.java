import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
public abstract class Framework {
    public List<String> stopWords(){
        List<String> stopWords=new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("../stop_words.txt"));
            String[] words = br.readLine().split(",");
            Collections.addAll(stopWords, words);
            br.close();
            for (char c = 'a'; c <= 'z'; c++) {
                stopWords.add(String.valueOf(c));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }
    public abstract List<String> words(String filePath);
    public abstract List<Map.Entry<String,Integer>> top25(List<String> wordList);
}
