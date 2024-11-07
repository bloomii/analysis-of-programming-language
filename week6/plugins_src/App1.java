import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App1 extends Framework {
    public List<String> words(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            br.close();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(sb.toString());
            String dataString = matcher.replaceAll(" ").toLowerCase().trim();
            String[] words=dataString.split("\\s+");
            List<String> resultWords=new ArrayList<>();
            List<String> stopWords=stopWords();
            for(String word:words){
                if(!stopWords.contains(word)){
                    resultWords.add(word);
                }
            }
            return resultWords;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Map.Entry<String, Integer>> top25(List<String> wordList) {
        Map<String,Integer> map=new HashMap<>();
        for(String word:wordList){
            map.put(word,map.getOrDefault(word,0)+1);
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list.subList(0,Math.min(25,list.size()));
    }
}
