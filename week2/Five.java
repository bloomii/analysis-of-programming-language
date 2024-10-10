import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Five {
    static class Pair{
        String name;
        int frequency;
        public Pair(String n,int f){
            name=n;
            frequency=f;
        }
    }
    static List<Character> data;
    static List<String> words;
    static List<Pair> wordFrequency;
    public static void main(String[] args) {
        data=new ArrayList<>();
        words=new ArrayList<>();
        wordFrequency=new ArrayList<>();
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        readFile(filePath);
        filterCharsAndNormalize();
        scan();
        removeStopWords();
        frequency();
        sort();
        for(int i=0;i<25&&i<wordFrequency.size();i++){
            System.out.println(wordFrequency.get(i).name+"  -  "+wordFrequency.get(i).frequency);
        }
    }
    private static void readFile(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line+="\n";
                for(char c:line.toCharArray()){
                    data.add(c);
                }
            }
            data.remove(data.size()-1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void filterCharsAndNormalize(){
        for(int i=0;i<data.size();i++){
            if(!Character.isLetterOrDigit(data.get(i))){
                data.set(i,' ');
            }else{
                data.set(i,String.valueOf(data.get(i)).toLowerCase().charAt(0));
            }
        }
    }
    private static void scan(){
        StringBuilder sb=new StringBuilder();
        for(char c:data){
            sb.append(c);
        }
        words.addAll(Arrays.asList(sb.toString().split("\\s+")));
    }
    private static void removeStopWords(){
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
        words=newWords;
    }
    private static void frequency(){
        for(String w:words) {
            List<String> keys = new ArrayList<>();
            for(Pair pair:wordFrequency){
                keys.add(pair.name);
            }
            if(keys.contains(w)){
                wordFrequency.get(keys.indexOf(w)).frequency++;
            }else{
                wordFrequency.add(new Pair(w,1));
            }
        }
    }
    private static void sort(){
        wordFrequency.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o2.frequency-o1.frequency;
            }
        });
    }

}
