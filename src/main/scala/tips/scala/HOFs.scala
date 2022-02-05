package tips.scala

// Higher Order Functions
object HOFs extends App {
  class Applicable {
    def apply(x: Int): Int = x + 1
  }

  val applicable = new Applicable
  val same       = applicable.apply(1) == applicable(1)
  println(same)

  // Function objects
  val incrementer1 = new Function[Int, Int] {
    def apply(x: Int): Int = x + 1
  }

  val sameToo = incrementer1.apply(1) == incrementer1(1)
  println(sameToo)

  // Syntax sugar
  val incrementer2 = (x: Int) => x + 2

  val sameAgain = incrementer2.apply(1) == incrementer2(1)
  println(sameAgain)

  // HOFs
  // 1. Accept function f from Int to Int and Int parameter n
  // 2. Return function that accept Int and return Int (from Int to Int)
  def nTimes(f: Int => Int, n: Int): Int => Int =
    if (n <= 0) identity
    else x => nTimes(f, n - 1)(f(x))

  println(nTimes(incrementer1, 3)(1))

  // Breakdown:
  // nTimes(f, 4) = x => nTimes(f, 3)(f(x)) == f(f(f(f(x))))
  // nTimes(f, 3) = x => nTimes(f, 2)(f(x)) == f(f(f(x)))
  // nTimes(f, 2) = x => nTimes(f, 1)(f(x)) == f(f(x))
  // nTimes(f, 1) = x => nTimes(f, 0)(f(x)) == f(x)
  // nTimes(f, 0) = identity                == x => x

  // When are these functions created?
  val f     = (x: Int) => x + 1
  val f4    = nTimes(f, 4)                   // f(f(f(f(x))))
  val f4Alt = (x: Int) => nTimes(f, 3)(f(x)) // new Function1[Int, Int] { def apply(x: Int): Int = nTimes(f, 3)(f(x)) }
  f(5) // All the function-objects will be created

  // How are the objects stored in memory?
  def nTimesOriginal(f: Function1[Int, Int], n: Int): Function1[Int, Int] =
    if (n <= 0) new Function[Int, Int] { def apply(x: Int): Int = x }                             // JVM object
    else new Function[Int, Int] { def apply(x: Int): Int = nTimesOriginal(f, n - 1).apply(f(x)) } // JVM object too

  // Isn't this memory wasteful?
}
