package global.vigil.codechallenge.aws.s3.util.file.stream

import zio.stream.ZPipeline

import scala.util.Try

object Pipeline {

  val decode = ZPipeline.utf8Decode

  val splitLines = ZPipeline.splitLines >>> ZPipeline.map[String, Array[String]](_.split("\\."))

  val ignoreHeadersIfExists = (separator: String) =>
    ZPipeline.dropWhile[Array[String]](lines =>
      !lines.headOption.exists(
        _.split(separator, 2)
          .take(2)
          .map(element => Try(element.trim.toInt.isValidInt).toOption.getOrElse(false))
          .forall(identity)
      )
    )

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

  val toLines = (separator: String) =>
    ZPipeline.map[List[(Int, Int)], Array[String]](
      _.map { case (key, repetition) =>
        s"$key$separator$repetition"
      }.toArray
    )

  val addSystemLineSeparator =
    ZPipeline.map[Array[String], String](_.mkString(System.lineSeparator()))

  val encode = ZPipeline.utf8Encode

}
