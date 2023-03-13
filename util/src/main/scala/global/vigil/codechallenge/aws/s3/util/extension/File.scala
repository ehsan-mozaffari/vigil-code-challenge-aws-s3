package global.vigil.codechallenge.aws.s3.util.extension

import zio.*

import java.io.File

object File {
  extension (str: String) {

    /***
     * Calculates the sv file separator based on thier types 
     * for example: csv will return ","
     * @return
     */
    def getSeparatorFromFileName: Option[String] = {
      val res = str.split("\\.")

      val fileTypeOpt = if (res.length < 2) None else res.lastOption

      fileTypeOpt.map(_.toLowerCase).flatMap {
        case "csv" => Some(",")
        case "tsv" => Some("\t")
        case _ => None
      }
    }

    /***
     * It gets list of formats and files in the path based on that. It is safe guarded by ZIO
     * @param formats csv and tsv for example 
     * @return
     */
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
