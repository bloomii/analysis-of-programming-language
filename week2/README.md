## week2

Here is
-   __Four.java__ which is a Java language implementation of [tf-04.py](https://github.com/crista/exercises-in-programming-style/blob/master/04-monolith/tf-04.py)
-   __Five.java__ which is a Java language implementation of [tf-05.py](https://github.com/crista/exercises-in-programming-style/blob/master/05-cookbook/tf-05.py)
-   __Six.java__ which is a Java language implementation of [tf-06.py](https://github.com/crista/exercises-in-programming-style/blob/master/06-pipeline/tf-06.py)
-   __Seven.java__ which is a Java language implementation of [tf-07.py](https://github.com/crista/exercises-in-programming-style/blob/master/07-code-golf/tf-07.py)

### How to run these code
#### run Four.java
```bash
cd ./week2
javac Four.java
java Four ../pride-and-prejudice.txt
```
if you want to test the __Four.java__, you can run code by there command:
```bash
cd ./week2
javac Four.java
java Four ../pride-and-prejudice.txt | diff - correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Five.java
```bash
cd ./week2
javac Five.java
java Five ../pride-and-prejudice.txt
```
if you want to test the __Five.java__, you can run code by there command:
```bash
cd ./week2
javac Five.java
java Five ../pride-and-prejudice.txt | diff - correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Six.java
```bash
cd ./week2
javac Six.java
java Six ../pride-and-prejudice.txt
```
if you want to test the __Six.java__, you can run code by there command:
```bash
cd ./week2
javac Six.java
java Six ../pride-and-prejudice.txt | diff - correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Seven.java
```bash
cd ./week2
javac Seven.java
java Seven ../pride-and-prejudice.txt
```
if you want to test the __Seven.java__, you can run code by there command:
```bash
cd ./week2
javac Seven.java
java Seven ../pride-and-prejudice.txt | diff - correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```