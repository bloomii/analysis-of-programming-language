## Week7

Here is
-   __TwentySeven.java__ which is a Java language implementation of [tf-27.py](https://github.com/crista/exercises-in-programming-style/blob/master/27-spreadsheet/tf-27.py)
-   __TwentyEight.java__ which is a Java language implementation of [tf-28.py](https://github.com/crista/exercises-in-programming-style/blob/master/28-lazy-rivers/tf-28.py)

### How to run these code
#### run TwentySeven.java
```bash
cd ./Week7
javac TwentySeven.java && java TwentySeven ../pride-and-prejudice.txt
```
if you want to test the __TwentySeven.java__, you can run code by there command:
```bash
cd ./Week7
javac TwentySeven.java && java TwentySeven ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

#### run TwentyEight.java
```bash
cd ./Week7
javac TwentyEight.java && java TwentyEight ../pride-and-prejudice.txt
```
if you want to test the __TwentyEight.java__, you can run code by there command:
```bash
cd ./Week7
javac TwentyEight.java && java TwentyEight ../pride-and-prejudice.txt | diff - ../correct_output.txt
```
it is correct if there is no output else incorrect.

### In the end
In the end, please run *rm* to remove class
```bash
rm *.class
```