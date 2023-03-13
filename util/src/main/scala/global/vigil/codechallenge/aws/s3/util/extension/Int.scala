package global.vigil.codechallenge.aws.s3.util.extension

object Int {

  extension (num: Int) {
    def isOdd: Boolean = num % 2 != 0
  }
}
