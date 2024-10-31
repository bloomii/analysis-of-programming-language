import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
class TFQuarantine{
    private List<Function> funcs;
    public TFQuarantine(Function func){
        funcs=new ArrayList<>();
        funcs.add(func);
    }
    public TFQuarantine bind(Function func){
        funcs.add(func);
        return this;
    }
    public void execute(String[] args){
        Function guardCallable = v -> {
            if (v instanceof Supplier) {
                return ((Supplier) v).get();
            }
            return v;
        };
        Object value=(Supplier)()->args;
        for(Function func:funcs){
            value= func.apply(guardCallable.apply(value));
        }
        System.out.print(guardCallable.apply(value));
    }
}
@SuppressWarnings("unchecked")
public class TwentyFive {
    private static Object getInput(Object argsObj){
        Supplier f=()->{
            String[] args=(String[])argsObj;
            return args[0];
//            return "../pride-and-prejudice.txt";
        };
        return f;
    }
    private static Object extractWords(Object filePathObj){
        Supplier f=()->{
            String filePath=String.valueOf(filePathObj);
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
            String dataString= sb.toString();
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(dataString);
            return matcher.replaceAll(" ").toLowerCase().trim().split(" ");
        };
        return f;
    }
    private static Object removeStopWords(Object wordsObj){
        Supplier f=()->{
            String[] words=(String[])wordsObj;
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
                throw new RuntimeException(e);
            }
            List<String> newWords=new ArrayList<>();
            for(String s:words){
                if(!stopWords.contains(s)){
                    newWords.add(s);
                }
            }
            return newWords;
        };
        return f;
    }
    private static Object frequencies(Object wordListObj){

        Map<String,Integer> wordFrequency=new HashMap<>();
        for(String word:(List<String>)wordListObj){
            if(wordFrequency.containsKey(word)){
                wordFrequency.put(word,wordFrequency.get(word)+1);
            }else{
                wordFrequency.put(word,1);
            }
        }
        return wordFrequency;
    }
    private static Object sort(Object wordFrequencyObj){
        Map<String,Integer> wordFrequency=(Map<String,Integer>)wordFrequencyObj;
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequency.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return list;
    }
    private static Object top25Frequencies(Object listObj){
        List<Map.Entry<String, Integer>> list = (List<Map.Entry<String, Integer>>) listObj;
        StringBuilder top25=new StringBuilder();
        for(int i=0;i<25&&i<list.size();i++){
            Map.Entry<String, Integer> entry=list.get(i);
            top25.append(entry.getKey() + "  -  " + entry.getValue()+"\n");
        }
        return top25.toString();
    }
    public static void main(String[] args){
        new TFQuarantine(TwentyFive::getInput)
                .bind(TwentyFive::extractWords)
                .bind(TwentyFive::removeStopWords)
                .bind(TwentyFive::frequencies)
                .bind(TwentyFive::sort)
                .bind(TwentyFive::top25Frequencies)
                .execute(args);
    }
}
