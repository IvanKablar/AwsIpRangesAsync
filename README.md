# AwsIpRangesAsync
This is a Webclient implementation that parses, maps, formats and displays a large Json file from the Amazon AWS Website, with minimal memory footprint.

The Json file is about 100 Megabyte of size. Usually, a mapping implemenation of the Json the Flux DataBuffer would require a heap of more than 100 Megabyte. 
This is due to the fact that all objects are loaded and mappend in memory while the list is being populated. With the DataBuffer implemenation a request to the WebClient requires a heap of only 18MB, because the parsing is done with an Java InputStream. To verify this statement do the following steps:

1. clone this project
2. build the jar file with ```gradle build```
3. change into the project directory ```build/libs```
4. execute the command: ```java -jar -Xmx18m AwsIpRanges-0.0.1-SNAPSHOT.jar```
5. open your browser and run ```http://localhost:8080/ipranges?region=all```

Doing so, you will get the whole Json diplayed in your browser, while the JVM reserves only a maximum of 18 Megabytes for the processing.
