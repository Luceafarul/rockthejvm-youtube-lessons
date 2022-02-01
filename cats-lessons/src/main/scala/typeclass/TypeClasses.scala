package typeclass

object TypeClasses {
  // Type classes - type system construct that support ad-hoc polymorphism

  trait Summable[T] {
    def sum(xs: List[T]): T
  }

  object Summable {
    def apply[T](implicit instance: Summable[T]): Summable[T] = instance
  }

  object SummableInstances {
    implicit object IntSummable extends Summable[Int] {
      def sum(xs: List[Int]): Int = xs.sum
    }

    implicit object StringSummable extends Summable[String] {
      def sum(xs: List[String]): String = xs.mkString
    }
  }

  // Problem: specialized implementation
  def processList[T](xs: List[T])(implicit summable: Summable[T]): T = // ad-hoc polymorphism
    // you want "sum up" all the elements of the list:
    // - for Int = sum is actual sum of all elements
    // - for String = sum is concatenation of all elements
    // - for other types - error
    summable.sum(xs) // <- here (ad-hoc)

  def process[T: Summable](xs: List[T]): T =
    Summable[T].sum(xs)

  def main(args: Array[String]): Unit = {
    import TypeClasses.SummableInstances._

    println(processList(List(1, 2, 3, 4)))
    println(processList(List("Scala ", "is ", "awesome")))
    println(process(List(1, 2, 3, 4)))
    println(process(List("Scala ", "is ", "awesome")))

    // println(processList(List('a', 'b', 'c')))
    // Does not compile with error:
    // could not find implicit value for parameter summable: typeclass.TypeClasses.Summable[Char]
  }
}
