## week3

Here is
-   __Eight.java__ which is a Java language implementation of [tf-08.py](https://github.com/crista/exercises-in-programming-style/blob/master/08-infinite-mirror/tf-08.py)
-   __Nine.java__ which is a Java language implementation of [tf-09.py](https://github.com/crista/exercises-in-programming-style/blob/master/09-kick-forward/tf-09.py)
-   __Ten.java__ which is a Java language implementation of [tf-10.py](https://github.com/crista/exercises-in-programming-style/blob/master/10-the-one/tf-10.py)

### How to run these code
#### run Eight.java
```bash
cd ./week3
javac Eight.java
java Eight ../pride-and-prejudice.txt
```
if you want to test the __Eight.java__, you can run code by there command:
```bash
cd ./week3
javac Eight.java
java Eight ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Nine.java
```bash
cd ./week3
javac Nine.java
java Nine ../pride-and-prejudice.txt
```
if you want to test the __Nine.java__, you can run code by there command:
```bash
cd ./week3
javac Nine.java
java Nine ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Ten.java
```bash
cd ./week3
javac Ten.java
java Ten ../pride-and-prejudice.txt
```
if you want to test the __Ten.java__, you can run code by there command:
```bash
cd ./week3
javac Ten.java
java Ten ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```