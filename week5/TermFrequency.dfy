datatype Pair = Pair(key: string, value: int)

method Main(){
    TermFrequency("White tigers live mostly in India\nWild lions live mostly in Africa\nSome acquaintance or other, my dear, I suppose; I am sure I do not\nknow.");
}

method TermFrequency(txt: string) 
requires txt != ""
{
    var wordLine := FilterCharsAndNormalize(txt);
    var stopWords:=["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","dear","did","do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","however","i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no","nor","not","of","off","often","on","only","or","other","our","own","rather","said","say","says","she","should","since","so","some","than","that","the","their","them","then","there","these","they","this","tis","to","too","twas","us","wants","was","we","were","what","when","where","which","while","who","whom","why","will","with","would","yet","you","your"];
    var words :=SplitByWhitespace(wordLine);
    var cleanedWords := RemoveStopWords(words,stopWords);
    var wordFrequency := Frequency(cleanedWords);
    PrintTop25(wordFrequency);
}
method SplitByWhitespace(wordLine: string) returns (resultWords: seq<string>)
requires forall i :: 0 <= i < |wordLine| ==> (wordLine[i]==' ' || 'a' <= wordLine[i] <= 'z')
ensures |resultWords| >= 0
{
  var result := [];
  var currentWord := [];
  var i := 0;
  while i < |wordLine| decreases |wordLine| - i
  {
    var c := wordLine[i];
    if c == ' ' {
        if |currentWord| > 0 {
            result := result + [currentWord];
            currentWord := []; 
        }
    } else {
        currentWord := currentWord + [c];
    }
    i := i+1;
  }
  if |currentWord| > 0 {
    result := result + [currentWord];
  }
  resultWords := result;
}
method FilterCharsAndNormalize(dataString: string) returns (words: string)
requires dataString!=""
ensures forall i :: 0 <= i < |words| ==> (words[i]==' ' || 'a' <= words[i] <= 'z')
{
    // Replace non-word characters with space and convert to lowercase
    // then split it by space
    var i := 0;
    words := "";
    while i < |dataString| decreases |dataString|-i
        invariant forall k:: 0<= k < |words| ==> (words[k]==' ' || 'a' <= words[k] <= 'z')
    {
        var currectChar:char :=' ';
        if 'a' <= dataString[i] <= 'z'
        {
            currectChar := dataString[i];
        } else if 'A' <= dataString[i] <= 'Z'
        {
            currectChar := dataString[i]+('a'-'A');
        }
        if currectChar == ' ' || ('a' <= currectChar <= 'z') {
            words := words + [currectChar];
        }
        i := i+1;
    }
}


method RemoveStopWords(words: seq<string>,stopWords: seq<string>) returns (cleanedWords: seq<string>)
requires |words|>=0 && |stopWords|>=0
ensures forall w ::  w in cleanedWords ==> w !in stopWords
{
    cleanedWords := [];
    var i := 0;
    while i < |words| decreases |words| - i 
        invariant 0<=i<=|words|
        invariant forall k:: 0<=k<|cleanedWords| ==> cleanedWords[k] in words && cleanedWords[k] !in stopWords
    {
        var word := words[i];
        if word !in stopWords {
            cleanedWords := cleanedWords + [word];
        }
        i := i + 1;
    }
}

method Frequency(words: seq<string>) returns (freqMap: seq<Pair>)
requires |words| >= 0
{
    freqMap := [];
    
    var j := 0;
    while j < |words| decreases |words|-j
        invariant j>=0
    {
        var i := 0;
        var found := 0;
        var word := words[j];
        while i < |freqMap| decreases |freqMap|-i
        {
            if freqMap[i].key == word {
                found := 1;
                freqMap := freqMap[..i] + [Pair(word,freqMap[i].value + 1)] + freqMap[i+1..];
                break;
            }    
            i := i+1;
        }
        if found == 0 {
            freqMap := freqMap + [Pair(word,1)];
        }
        j := j+1;
    }
    var i:=0;
    while i < |freqMap| decreases |freqMap| -i
        invariant 0 <= i <= |freqMap|
    {
        var j:= i+1;
        //var maxV:=freqMap[i].value;
        var maxIndex:=i;
        while j < |freqMap| decreases |freqMap| -j
            invariant i < j <= |freqMap| && i <= maxIndex < |freqMap| 
            invariant maxIndex>i ==> freqMap[maxIndex].value >= freqMap[i].value
        {
            if freqMap[maxIndex].value < freqMap[j].value
            {
                //maxV := freqMap[j].value;
                maxIndex := j;
            }
            j:=j+1;
        }
        if maxIndex != i
        {
            freqMap := freqMap[..i] + [freqMap[maxIndex]] + freqMap[i+1..maxIndex] + [freqMap[i]] + freqMap[maxIndex+1..];
        }
        i := i+1;
    }
}

method DigitToChar(digit: int) returns (r: char)
{  
    r:=' ';
    if digit == 0 {r:='0';} else  
    if digit == 1 {r:= '1';} else  
    if digit == 2 {r:= '2';} else  
    if digit == 3 {r:= '3';} else  
    if digit == 4 {r:= '4';} else  
    if digit == 5 {r:= '5';} else  
    if digit == 6 {r:= '6';} else  
    if digit == 7 {r:= '7';} else  
    if digit == 8 {r:= '8';} else  
    if digit == 9 {r:= '9';}   
}
method PrintTop25(wordFreqs: seq<Pair>) 
requires |wordFreqs|>=0
{
    var i := 0;
    while i < |wordFreqs| && i < 25 decreases |wordFreqs|-i
    {
        var x:int := wordFreqs[i].value;
        var s := [];
        while x > 0 decreases x
        {
            var digit: int := x % 10;       // Get the last digit of x
            var b: char := DigitToChar(digit); // Convert the digit to its character representation
            s := [b] + s;
            x := x / 10;
        }
        print(wordFreqs[i].key + " - " + s + "\n");
        i := i+1;
    }
}



