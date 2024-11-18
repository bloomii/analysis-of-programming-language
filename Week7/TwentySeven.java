import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Column{
    public Object value;
    public Supplier<Object> func;
    public Column(Object c0,Supplier<Object> c1){
        this.value=c0;
        this.func=c1;
    }
}
@SuppressWarnings("unchecked")
public class TwentySeven {
    private static void update(Column[] columns){
        for(Column column:columns){
            if(column.func!=null){
                column.value=column.func.get();
            }
        }
    }
    public static void main(String[] args){
//        String filePath="../pride-and-prejudice.txt";
        String filePath=args[0];
        Column allWords=new Column(null,null);
        Column stopWords=new Column(null,null);
        Column nonStopWords=new Column(null,()->{
            List<String> result=new ArrayList<>();
            Set<String> stopWordsSet=(Set<String>)stopWords.value;
            for(String word:(List<String>)allWords.value){
                if(!stopWordsSet.contains(word)){
                    result.add(word);
                }
            }
            return result;
        });
        Column uniqueWords=new Column(null,()->{
           return new HashSet<>((List<String>)nonStopWords.value);
        });

        Column counts=new Column(null,()->{
            Map<String,Integer> wordFrequency=new HashMap<>();
            for(String word:(Set<String>)uniqueWords.value){
                wordFrequency.put(word,Collections.frequency((List<String>)nonStopWords.value,word));
            }
            return wordFrequency;
        });

        Column sortedData=new Column(null,()->{
            List<Map.Entry<String, Integer>> list = new ArrayList<>(((Map<String,Integer>)(counts.value)).entrySet());
            list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
            return list;
        });

        Column[] columns=new Column[]{allWords,stopWords,nonStopWords,uniqueWords,counts,sortedData};
        try {
            String fileText=new String(Files.readAllBytes(Paths.get(filePath)));
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(fileText);
            String[] words = matcher.replaceAll(" ").toLowerCase().trim().split("\\s+");

            allWords.value= Arrays.stream(words).toList();
        } catch (IOException e) {
            System.out.println(e);
        }

        Set<String> stopWordsList=new HashSet<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("../stop_words.txt"));
            String[] words = br.readLine().split(",");
            Collections.addAll(stopWordsList, words);
            br.close();
            for (char c = 'a'; c <= 'z'; c++) {
                stopWordsList.add(String.valueOf(c));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        stopWords.value=stopWordsList;
        update(columns);
        List<Map.Entry<String, Integer>> list=(List<Map.Entry<String, Integer>>)sortedData.value;
        for (int i = 0; i < list.size()&&i<25; i++) {
            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
        }
        System.out.println("Enter new name of file of words:");
        Scanner scanner=new Scanner(System.in);
        String file=scanner.nextLine();
        try {
            String fileText=new String(Files.readAllBytes(Paths.get(file)));
            Pattern pattern = Pattern.compile("[\\W_]+");
            Matcher matcher = pattern.matcher(fileText);
            String[] words = matcher.replaceAll(" ").toLowerCase().trim().split("\\s+");

            allWords.value= Arrays.stream(words).toList();
        } catch (IOException e) {
            allWords.value=new ArrayList<>();
            System.out.println(e);
        }
        update(columns);
        list=(List<Map.Entry<String, Integer>>)sortedData.value;
        for (int i = 0; i < list.size()&&i<25; i++) {
            System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
        }
    }
}
