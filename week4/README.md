## week4

Here is
-   __Twelve.java__ which is a Java language implementation of [tf-12.py](https://github.com/crista/exercises-in-programming-style/blob/master/12-letterbox/tf-12.py)
-   __Thirteen.java__ which is a Java language implementation of [tf-13.py](https://github.com/crista/exercises-in-programming-style/blob/master/13-closed-maps/tf-13.py)
-   __Fifteen.java__ which is a Java language implementation of [tf-15.py](https://github.com/crista/exercises-in-programming-style/blob/master/15-hollywood/tf-15.py)

### How to run these code
#### run Twelve.java
```bash
cd ./week4
javac Twelve.java
java Twelve ../pride-and-prejudice.txt
```
if you want to test the __Twelve.java__, you can run code by there command:
```bash
cd ./week4
javac Twelve.java
java Twelve ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Thirteen.java
```bash
cd ./week4
javac Thirteen.java
java Thirteen ../pride-and-prejudice.txt
```
if you want to test the __Thirteen.java__, you can run code by there command:
```bash
cd ./week4
javac Thirteen.java
java Thirteen ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Fifteen.java
```bash
cd ./week4
javac Fifteen.java
java Fifteen ../pride-and-prejudice.txt
```
if you want to test the __Fifteen.java__, you can run code by there command:
```bash
cd ./week4
javac Fifteen.java
java Fifteen ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```