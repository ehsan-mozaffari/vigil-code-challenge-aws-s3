package global.vigil.codechallenge.aws.s3.util.file.stream

import zio.stream.ZSink
import global.vigil.codechallenge.aws.s3.util.extension.Int.*


object Sink {

  val saveFile = (toFileName: String) => ZSink.fromFileName(toFileName)

  val collectEachOdd: ZSink[Any, Nothing, List[(Int, Int)], Nothing, Map[Int, List[Int]]] =
    ZSink
      .collectAllToMap[List[(Int, Int)], Int](_.head._1)(_ ::: _)
      .map(_.map { case (key, value) =>
        key -> value.map(_._2)
      }.filter { case (_, value) =>
        value.size.isOdd
      })


}
