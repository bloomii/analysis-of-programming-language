import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Four {
    static class Pair{
        String name;
        int frequency;
        public Pair(String n,int f){
            name=n;
            frequency=f;
        }
    }
    public static void main(String[] args) {
        List<String> stopWords = loadStopWords("../stop_words.txt");
        List<Pair> wordFrequency=new ArrayList<>();
        String filePath = args[0];
//        String filePath = "../pride-and-prejudice.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line+="\n";
                Integer startChar=null;
                int i=0;
                for(char c:line.toCharArray()){
                    if (startChar == null) {
                        if (isAlNum(c)) {
                            startChar = i;
                        }
                    }else{
                        if(!isAlNum(c)){
                            boolean found = false;
                            String word=line.substring(startChar,i).toLowerCase();
                            if(!stopWords.contains(word)){
                                int pairIndex = 0;
                                for(Pair pair:wordFrequency){
                                    if(pair.name.equals(word)){
                                        pair.frequency++;
                                        found=true;
                                        break;
                                    }
                                    pairIndex++;
                                }
                                if(!found){
                                    wordFrequency.add(new Pair(word,1));
                                } else if (wordFrequency.size()>1) {
                                    for(int n=pairIndex-1;n>=0;n--){
                                        if(wordFrequency.get(pairIndex).frequency >
                                                wordFrequency.get(n).frequency){
                                            Pair t=wordFrequency.get(pairIndex);
                                            wordFrequency.set(pairIndex,wordFrequency.get(n));
                                            wordFrequency.set(n,t);
                                            pairIndex=n;
                                        }
                                    }
                                }
                            }
                            startChar = null;
                        }
                    }
                    i++;
                }

            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        for(int i=0;i<25&&i<wordFrequency.size();i++){
            System.out.println(wordFrequency.get(i).name+"  -  "+wordFrequency.get(i).frequency);
        }
    }
    private static boolean isAlNum(char c){
        return Character.isLetterOrDigit(c);
    }
    private static List<String> loadStopWords(String filePath) {
        List<String> stopWords = new ArrayList<>();
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
