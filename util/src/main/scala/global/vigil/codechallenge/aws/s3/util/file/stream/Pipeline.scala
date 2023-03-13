package global.vigil.codechallenge.aws.s3.util.file.stream

import zio.stream.ZPipeline

import scala.util.Try

object Pipeline {

  /**
   * decode the file based on UTF8
   */
  val decode = ZPipeline.utf8Decode

  /**
   * Splits the decoded string and transform it into array of lines 
   */
  val splitLines = ZPipeline.splitLines >>> ZPipeline.map[String, Array[String]](_.split("\\."))

  /**
   * check if the csv or tsv files have headers and drop them
   */
  val ignoreHeadersIfExists = (separator: String) =>
    ZPipeline.dropWhile[Array[String]](lines =>
      !lines.headOption.exists(
        _.split(separator, 2)
          .take(2)
          .map(element => Try(element.trim.toInt.isValidInt).toOption.getOrElse(false))
          .forall(identity)
      )
    )

  /**
   * transform the raw array of lines to a list with a key value with the certain business
   * It supports integer two columns files and leave 0 instead of blanks in the file.
   * @param pass the separator of the file
   */
  val toKeyValueList = (separator: String) =>
    ZPipeline.map[Array[String], List[(Int, Int)]](
      _.map(
        _.split(separator, 3)
          .take(2)
          .toList
          .map(_.trim)
          .map(x => if (x.isBlank) 0 else x.toInt) match {
          case List(col1)           => col1 -> 0
          case List(col1, col2)     => col1 -> col2
          case List(col1, col2, _*) => col1 -> col2
          case _                    => 0    -> 0
        }
      ).toList
    )

  /***
   * transforms from the list of key value to array of raw string with the help of separator
   * @param separator of the file
   */
  val toLines = (separator: String) =>
    ZPipeline.map[List[(Int, Int)], Array[String]](
      _.map { case (key, repetition) =>
        s"$key$separator$repetition"
      }.toArray
    )

  /**
   * Adds line separator based on the os type
   */
  val addSystemLineSeparator =
    ZPipeline.map[Array[String], String](_.mkString(System.lineSeparator()))

  /**
   * Encode the file to prepare it for saving in the Sink
   */
  val encode = ZPipeline.utf8Encode

}
