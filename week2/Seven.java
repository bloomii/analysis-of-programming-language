import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Seven {
    public static void main(String[] args) throws IOException {
        List<String> stopWords=Arrays.asList((new String(Files.readAllBytes(Paths.get("../stop_words.txt")))+",a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z").split(","));
        Arrays.stream(Pattern.compile("[\\W_]+").matcher(new String(Files.readAllBytes(Paths.get(args[0])))).replaceAll(" ").toLowerCase().trim().split("\\s+")).filter(word -> !stopWords.contains(word)).collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum)).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(25).forEach(entry-> System.out.println(entry.getKey()+"  -  "+entry.getValue()));
    }
}
