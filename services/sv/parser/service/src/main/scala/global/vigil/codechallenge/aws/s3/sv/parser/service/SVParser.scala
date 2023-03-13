package global.vigil.codechallenge.aws.s3.sv.parser.service

import global.vigil.codechallenge.aws.s3.util.extension.File.*
import global.vigil.codechallenge.aws.s3.util.file.stream.Pipeline.*
import global.vigil.codechallenge.aws.s3.util.file.stream.Sink.*
import global.vigil.codechallenge.aws.s3.util.file.stream.Stream.*
import zio.*
import zio.stream.ZStream

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SVParser {

  private def fileOddNumbersFilter(
      fileName:  String,
      separator: String
  ): ZIO[Any, Throwable, Map[Int, List[Int]]] = {
    fetchFile(fileName)
      .via(
        decode
          >>>
            splitLines
            >>>
            ignoreHeadersIfExists(separator)
            >>>
            toKeyValueList(separator)
      )
      .run(collectEachOdd)
  }

  /***
   * It gets input and output paths and save a stream result_timestamp.tsv in the output
   * path. It fetches all files with csv and tsv type and process them parallel without blocking
   * and save the file in a stream in the safe way
   * @param inputPath
   * @param outputPath
   * @return
   */
  def calculateOddNumbersAllInPath(
      inputPath:  String,
      outputPath: String
  ): ZIO[Any, Throwable, Long] = {

    val filesPath = inputPath.getFilesPathFromDir(List("CSV", "TSV"))

    ZStream
      .fromZIO(filesPath)
      .mapZIOPar(10) { fileNames =>
        ZIO
          .foreachPar(fileNames) { fn =>
            fileOddNumbersFilter(fn, fn.getSeparatorFromFileName.getOrElse(","))
          }
          .map(_.flatMap(_.map { case (key, values) =>
            key -> values.size
          }.toList)) // Converts Map to list
      }
      .via(
        toLines("\t")
          >>>
            addSystemLineSeparator
            >>>
            encode
      )
      .run(
        saveFile(
          s"$outputPath/result_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.tsv"
        )
      )
      .logError
  }
}
