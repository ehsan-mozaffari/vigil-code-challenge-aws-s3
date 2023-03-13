package global.vigil.codechallenge.aws.s3.sv.parser

import global.vigil.codechallenge.aws.s3.sv.parser.service.SVParser
import zio.*


object Main extends ZIOAppDefault {
  override def run = {

    val defaultInputPath = "services/src/main/scala/resources/test"
    val defaultOutputPath = "services/src/main/scala/resources"
    
    getArgs.flatMap {
      case Chunk(inputPath, outputPath) =>  SVParser.calculateOddNumbersAllInPath(inputPath, outputPath)
      case _ =>  SVParser.calculateOddNumbersAllInPath(defaultInputPath, defaultOutputPath)
    }.unit.exitCode
  }
}
