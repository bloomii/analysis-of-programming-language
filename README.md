# analysis of programming language
click [hear](https://replit.com/@zzwwcc12301/analysis-of-programming-language) to view this project page of replit
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