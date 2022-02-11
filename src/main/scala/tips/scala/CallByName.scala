package tips.scala

import scala.util.Try

object CallByName extends App {
  def byValue(x: Int): Int = 73

  // When call with some expression, like 2 + 3
  // then expression evaluate first and then
  // pass as param -> byValue(5)
  byValue(2 + 3)

  def byName(x: => Int): Int = 73

  // When call with some expression, like 2 + 3
  // expression 2 + 3 is passed literally
  // and evaluate only if called inside method -> byName(2 + 3)
  byName(2 + 3)

  // Tricks
  // 1. Re-evaluation
  def byValuePrintln(x: Long): Unit = {
    println(x)
    println(x)
  }

  def byNamePrintln(x: => Long): Unit = {
    println(x)
    println(x)
  }

  byValuePrintln(System.nanoTime()) // printed same value two times
  byNamePrintln(System.nanoTime())  // printed different values

  // 2. Call by need
  // Useful for infinite collections like LazyList
  abstract class List[+T] {
    def head: T
    def tail: List[T]
  }
  class NonEmptyList[+T](h: => T, t: => List[T]) extends List[T] {
    lazy val head: T       = h
    lazy val tail: List[T] = t
  }

  // 3. Hold the door
  val attempt: Try[Int] = Try { // can use {} and it's looks like part of the language
    throw new IllegalArgumentException
  }
}
