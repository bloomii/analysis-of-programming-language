import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Eight {
    static final int RECURSION_LIMIT=5000;
    public static void main(String[] args){
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        Set<String> stopWords=loadStopWords("../stop_words.txt");
        List<String> wordList=readFile(filePath);
        Map<String,Integer> wordFrequency=new HashMap<>();
        for(int i=0;i<wordList.size();i+=RECURSION_LIMIT) {
            count(wordList.subList(i,Math.min(i+RECURSION_LIMIT,wordList.size())), stopWords, wordFrequency);
        }
        List<String>sortedKey=sort(wordFrequency);
        wordFrequencyPrint(sortedKey.subList(0,25),wordFrequency);
    }
    private static void count(List<String> wordList, Set<String> stopWords, Map<String,Integer> wordFrequency){
        if(wordList.size()==0){
            return;
        }else{
            String word = wordList.get(0);
            if(!stopWords.contains(word)){
                if(wordFrequency.containsKey(word)){
                    wordFrequency.put(word, wordFrequency.get(word)+1);
                }else{
                    wordFrequency.put(word, 1);
                }
            }
            count(wordList.subList(1,wordList.size()),stopWords,wordFrequency);
        }
    }
    private static void wordFrequencyPrint(List<String> sortedKey,Map<String, Integer> wordFreqs){
        if (sortedKey.size() == 0) {
            return;
        }else{
            System.out.println(sortedKey.get(0) + "  -  " + wordFreqs.get(sortedKey.get(0)));
            wordFrequencyPrint(sortedKey.subList(1, sortedKey.size()),wordFreqs);
        }
    }
    private static List<String> readFile(String filePath){
        List<String> re=new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            Pattern pattern = Pattern.compile("[a-z]{2,}");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.toLowerCase());
                while (matcher.find()) {
                    String word = matcher.group();
                    re.add(word);
                }
            }
            reader.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return re;
    }

    /**
     * Use recursion to implement merger sorting. Every time,
     * select the largest one in the map and add it to the list
     * @param list result list
     * @param wordFrequency map of word frequency
     */
    private static void mergerSort(List<String> list,int start,int end,Map<String,Integer> wordFrequency){
        if(start>=end){
            return;
        }
        int mid=(start+end)/2;
        mergerSort(list,start,mid,wordFrequency);
        mergerSort(list,mid+1,end,wordFrequency);
        List<String> re=new ArrayList<>();
        int i=start,j=mid+1;
        while(i<=mid&&j<=end){
            if(wordFrequency.get(list.get(i))<wordFrequency.get(list.get(j))){
                re.add(list.get(j));
                j++;
            }else{
                re.add(list.get(i));
                i++;
            }
        }
        while(i<=mid){
            re.add(list.get(i));
            i++;
        }
        while(j<=end){
            re.add(list.get(j));
            j++;
        }
        for(int k=start;k<=end;k++){
            list.set(k,re.get(k-start));
        }
    }
    private static List<String> sort(Map<String,Integer> wordFrequency){
        List<String> list = new ArrayList<>(wordFrequency.keySet());
        mergerSort(list,0,list.size()-1,wordFrequency);
        return list;
    }
    private static Set<String> loadStopWords(String filePath) {
        Set<String> stopWords = new HashSet<>();
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
        return stopWords;
    }

}
