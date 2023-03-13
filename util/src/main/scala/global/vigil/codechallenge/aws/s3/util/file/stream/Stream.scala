package global.vigil.codechallenge.aws.s3.util.file.stream

import zio.stream.ZStream

object Stream {

  /***
   * Fetches a file in a stream way
   */
  val fetchFile = (fromFileName: String) => ZStream.fromFileName(fromFileName)
}
