## Week8

Here is
-   __TwentyNine.java__ which is a Java language implementation of [tf-29.py](https://github.com/crista/exercises-in-programming-style/blob/master/29-actors/tf-29.py)
-   __Thirty.java__ which is a Java language implementation of [tf-30.py](https://github.com/crista/exercises-in-programming-style/blob/master/30-dataspaces/tf-30.py)
-   __ThirtyTwo.java__ which is a Java language implementation of [tf-32.py](https://github.com/crista/exercises-in-programming-style/blob/master/32-double-map-reduce/tf-32.py)

### How to run these code
#### run TwentyNine.java
```bash
cd ./Week8
javac TwentyNine.java && java TwentyNine ../pride-and-prejudice.txt
```
if you want to test the __TwentyNine.java__, you can run code by there command:
```bash
cd ./Week8
javac TwentyNine.java && java TwentyNine ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run Thirty.java
```bash
cd ./Week8
javac Thirty.java && java Thirty ../pride-and-prejudice.txt
```
if you want to test the __Thirty.java__, you can run code by there command:
```bash
cd ./Week8
javac Thirty.java && java Thirty ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run ThirtyTwo.java
```bash
cd ./Week8
javac ThirtyTwo.java && java ThirtyTwo ../pride-and-prejudice.txt
```
if you want to test the __Thirty.java__, you can run code by there command:
```bash
cd ./Week8
javac ThirtyTwo.java && java ThirtyTwo ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```