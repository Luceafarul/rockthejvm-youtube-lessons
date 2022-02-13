package tips.scala

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import java.util.Scanner
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

object ReadFiles extends App {
  val filePath = "src/main/resources/song.txt"

  // Version 1: Java way
  val file    = new File(filePath)
  val scanner = new Scanner(file)
  while (scanner.hasNextLine) println(scanner.nextLine)

  // Version 2: Java way with commons-io
  val fileContents = FileUtils.readLines(file, Charset.defaultCharset())
  fileContents.forEach(println)

  // Version 3: Scala way
  val contents = Source.fromResource("song.txt").getLines()
  contents.foreach(println)

  // Version 4: RichFile
  def open(path: String) = new File(path)
  implicit class RichFile(file: File) {
    def read: Try[List[String]] =
      Using(Source.fromFile(file))(_.getLines().toList)
  }

  val readFile = open(filePath).read
  readFile match {
    case Failure(exception) => println(s"Can't read file, reason: $exception")
    case Success(content)   => content.foreach(println)
  }
}
