## week6

Here is
-   __Seventeen.java__ which is a Java language implementation of [tf-11.py](https://github.com/crista/exercises-in-programming-style/blob/master/11-things/tf-11.py)
-   __Twenty.java__ which is a Java language implementation of [20-plugins](https://github.com/crista/exercises-in-programming-style/blob/master/20-plugins/)

### How to run these code
#### run Seventeen.java
```bash
cd ./week6
javac Seventeen.java
java Seventeen ../pride-and-prejudice.txt
```

#### run Twenty.java
```bash
cd ./week6
javac -cp ./plugins/framework.jar Twenty.java
java -cp .:./plugins/framework.jar:./plugins/app1.jar:./plugins/app2.jar Twenty ../pride-and-prejudice.txt
```

In __config.properties__, there are default value "./plugins/app1.jar" to use the app1.jar.
If you want to use the app2.jar,please replace "./plugins/app1.jar" to "./plugins/app2.jar"
These two jar packages have the same functionality, but they are implemented in different ways.

if you want to test the __Twenty.java__, you can run code by there command:
```bash
cd ./week6
javac -cp ./plugins/framework.jar Twenty.java
java -cp .:./plugins/framework.jar:./plugins/app1.jar:./plugins/app2.jar Twenty ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.
##### how to make this jar
if you want to make __framework.jar__, just run code by there command:
```bash
cd ./week6/plugins_src
javac Framework.java
jar cvf ../plugins/framework.jar Framework.class
```
if you want to make __app1.jar__, just run code by there command:
```bash
cd ./week6/plugins_src
javac -cp ../plugins/framework.jar App1.java
jar cvf ../plugins/app1.jar App1.class
```
if you want to make __app2.jar__, just run code by there command:
```bash
cd ./week6/plugins_src
javac -cp ../plugins/framework.jar App2.java
jar cvf ../plugins/app2.jar App2.class
```
### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```