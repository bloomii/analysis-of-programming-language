import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Twenty {
    private static Properties getProperties(){
        Properties config = new Properties();
        try {
            FileInputStream configFile = new FileInputStream("config.properties");
            config.load(configFile);
            configFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return config;
    }
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Please provide a file path.");
            return;
        }
        try {
            Properties config = getProperties();
            Framework framework=loadJarAndClass(config.getProperty("jar.path"));
            List<String> words=framework.words(args[0]);
            List<Map.Entry<String, Integer>> list=framework.top25(words);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getKey() + "  -  " + list.get(i).getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static Framework loadJarAndClass(String jarPath) throws Exception {
        URL jarUrl = new URL("file://" + System.getProperty("user.dir")+"/"+jarPath);
        URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl });
        String className=findClassNameInJar(jarUrl);

        return (Framework) classLoader.loadClass(className).newInstance();
    }
    public static String findClassNameInJar(URL jarUrl) throws IOException {
        JarFile jarFile = new JarFile(jarUrl.getFile());
        Enumeration<JarEntry> entries = jarFile.entries();
        String className = null;
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entryName.endsWith(".class")) {
                className = entryName.replace('/', '.').substring(0, entryName.length() - 6); // 去掉 .class 后缀
                break;
            }
        }
        jarFile.close();
        return className;
    }
}
