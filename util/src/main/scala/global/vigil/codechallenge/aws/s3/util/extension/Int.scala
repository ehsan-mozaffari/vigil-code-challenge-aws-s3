package global.vigil.codechallenge.aws.s3.util.extension

object Int {
  
  extension (num: Int) {

    /***
     * It calculates if the number is odd
     * @return boolean
     */
    def isOdd: Boolean = num % 2 != 0
  }
}
