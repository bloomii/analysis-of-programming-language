import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("unchecked")
public class Thirteen {
    private static void extractWords(Map<String, Object> obj, String filePath){
//        String filePathStr=String.valueOf(filePath);
        StringBuilder sb=new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line+="\n";
                sb.append(line);
            }
            String data=sb.toString();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(data);
            String[] dataStrings=matcher.replaceAll(" ").toLowerCase().trim().split(" ");
            obj.put("data",dataStrings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void loadStopWords(Map<String,Object> obj){
        List<String> stopWords=new ArrayList<>();
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
        obj.put("stop_words",stopWords);
    }
    private static void incrementCount(Map<String,Object> obj,String word){
        Map<String,Integer> wordFrequency= (Map<String, Integer>) obj.get("freqs");
        wordFrequency.put(word, wordFrequency.getOrDefault(word,0)+1);
    }
    public static void main(String[] args){
        Map<String,Object> dataStorageObj=new HashMap<>();
        dataStorageObj.put("data",new ArrayList<>());
        dataStorageObj.put("init",(Consumer<String>)(filePath)->{extractWords(dataStorageObj, filePath);});
        dataStorageObj.put("words",(Supplier<String[]>)()-> {return (String[]) dataStorageObj.get("data");});

        Map<String,Object> stopWordsObj=new HashMap<>();
        stopWordsObj.put("stop_words",new ArrayList<>());
        stopWordsObj.put("init",(Runnable)()->{loadStopWords(stopWordsObj);});
        stopWordsObj.put("is_stop_word",(Function<String,Boolean>)(word)-> {return ((List<String>)(stopWordsObj.get("stop_words"))).contains(word);});

        Map<String,Object> wordFrequencyObj=new HashMap<>();
        wordFrequencyObj.put("freqs",new HashMap<>());
        wordFrequencyObj.put("increment_count",(Consumer<String>)(word)->{incrementCount(wordFrequencyObj, word);});
        wordFrequencyObj.put("sorted",(Supplier<List<Map.Entry<String, Integer>>>)()->{
            List<Map.Entry<String, Integer>> list = new ArrayList<>(((Map<String, Integer>)(wordFrequencyObj.get("freqs"))).entrySet());
            list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            return list;
        });
        wordFrequencyObj.put("top25",(Runnable)()->{
            List<Map.Entry<String, Integer>> list=((Supplier<List<Map.Entry<String, Integer>>>)(wordFrequencyObj.get("sorted"))).get();
            for (int i=0;i<25&&i< list.size();i++) {
                System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
            }
        });

//        ((Consumer<String>)(dataStorageObj.get("init"))).accept("../pride-and-prejudice.txt");
        ((Consumer<String>)(dataStorageObj.get("init"))).accept(args[0]);
        ((Runnable)(stopWordsObj.get("init"))).run();
        for(String word:((Supplier<String[]>)(dataStorageObj.get("words"))).get()) {
            if(!((Function<String,Boolean>)(stopWordsObj.get("is_stop_word"))).apply(word)){
                ((Consumer<String>)(wordFrequencyObj.get("increment_count"))).accept(word);
            }
        }
        ((Runnable)wordFrequencyObj.get("top25")).run();
//        List<Map.Entry<String, Integer>> list=((Supplier<List<Map.Entry<String, Integer>>>)(wordFrequencyObj.get("sorted"))).get();
//        for (int i=0;i<25&&i< list.size();i++) {
//            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
//        }
    }


}
