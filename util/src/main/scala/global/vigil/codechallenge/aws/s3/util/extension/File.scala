package global.vigil.codechallenge.aws.s3.util.extension

import zio.*

import java.io.File

object File {
  extension (str: String) {

    def getSeparatorFromFileName: Option[String] = {
      val res = str.split("\\.")

      val fileTypeOpt = if (res.length < 2) None else res.lastOption

      fileTypeOpt.map(_.toLowerCase).flatMap {
        case "csv" => Some(",")
        case "tsv" => Some("\t")
        case _ => None
      }
    }

    def getFilesPathFromDir(formats: List[String]): Task[List[String]] =
      ZIO.attemptBlocking {
        val dirPath: File = new File(str)
        val filesPath: List[String] = dirPath.listFiles().toList.map(_.getPath)
        val fileNamesFilteredByFormats =
          filesPath.filter(fp =>
            formats.exists(f => fp.toLowerCase.endsWith(s".${f.toLowerCase}"))
          )
        fileNamesFilteredByFormats
      }
  }
}
