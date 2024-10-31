## week5

Here is
-   __TwentyOne.java__ which is a Java language implementations and add error detection and handling in the required [style 21](https://github.com/crista/exercises-in-programming-style/blob/master/21-constructivist/README.md).
-   __TwentyTwo.java__ which is a Java language implementations and add error detection and handling in the required [style 22](https://github.com/crista/exercises-in-programming-style/blob/master/22-tantrum/README.md).
-   __TwentyFive.java__ which is a Java language implementations and add error detection and handling in the required [style 25](https://github.com/crista/exercises-in-programming-style/blob/master/25-quarantine/README.md).

### How to run these code
#### run TwentyOne.java
```bash
cd ./week5
javac TwentyOne.java
java TwentyOne ../pride-and-prejudice.txt
```
if you want to test the __TwentyOne.java__, you can run code by there command:
```bash
cd ./week5
javac TwentyOne.java
java TwentyOne ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run TwentyTwo.java
```bash
cd ./week5
javac TwentyTwo.java
java TwentyTwo ../pride-and-prejudice.txt
```
if you want to test the __TwentyTwo.java__, you can run code by there command:
```bash
cd ./week5
javac TwentyTwo.java
java TwentyTwo ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run TwentyFive.java
```bash
cd ./week5
javac TwentyFive.java
java TwentyFive ../pride-and-prejudice.txt
```
if you want to test the __TwentyFive.java__, you can run code by there command:
```bash
cd ./week5
javac TwentyFive.java
java TwentyFive ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```