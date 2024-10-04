# analysis of programming language

## week1
you can run code by this commant. And then, the program will print the top25 word count.
```bash
cd ./week1
javac wordCount.java
java wordCount ../pride-and-prejudice.txt
```

if you want to test the program, you can run code by there commant:
```bash
cd ./week1
javac wordCount.java
java wordCount ../pride-and-prejudice.txt | diff - correct_output.txt
```
it is correct if there is no output else incorrect.

In the end, please run *rm* to romove class
```bash
rm *.class
```