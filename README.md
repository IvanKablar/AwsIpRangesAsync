# AwsIpRangesAsync
This is a Webclient implementation that parses, maps, formats and displays a large Json with minimal memory footprint file from the Amazon AWS Website.

The Json file is about 100 Megabyte of size. Usually, an implemenation of the Json mapping without the Flux DataBuffer would require a heap of more than 100 Megabyte to parse and display the Json. 
The reason for this is, when you work with a Json data model the whole Json is loaded into memory when the objects are mappend and the list is being populated. With the DataBuffer implemenation a request to the WebClient requires a heap of only 18MB. 
To verify this statement do the following steps:

1. clone this project
2. build the jar file with ```gradle build```
3. change into the project directory ```build/libs```
4. execute the command: ```java -jar -Xmx18m AwsIpRanges-0.0.1-SNAPSHOT.jar```
5. open your browser and run ```http://localhost:8080/ipranges?region=all```

Doing so, you will get the whole Json diplayed in your browser, while the JVM reserves only a maximum of 18 Megabytes for the processing.
