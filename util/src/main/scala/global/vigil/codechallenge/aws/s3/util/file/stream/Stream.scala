package global.vigil.codechallenge.aws.s3.util.file.stream

import zio.stream.ZStream

object Stream {

  val fetchFile = (fromFileName: String) => ZStream.fromFileName(fromFileName)
}
