# vigil-code-challenge-aws

This project is written by ZIO and ZIO stream. It uses highly scalable approach to read a file 
as a multi-chunk and stream process it in parallel for multiple files and saves like stream to a file.
So in terms of memory it is just a stream with chunks in memory. You could process massive files without
consuming more memory with back pressure and all other stream options.

the complexity is liner and just putting into a hashmap takes O(n/m) and in overall is O(N).

## Prerequisites
1. install git
2. Java 19
3. Scala 3

## Getting Started

please run sbt services/run with your in put and output directory:
```scala
sbt "services/run <input_path> <output_path>"
sbt "services/run services/src/main/scala/resources/test services/src/main/scala/resources"
```