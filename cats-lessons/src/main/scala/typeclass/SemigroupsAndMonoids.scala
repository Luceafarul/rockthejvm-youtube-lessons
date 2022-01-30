package typeclass

object SemigroupsAndMonoids extends App {
  // Type + a combination
  // 1. Trait
  // 2. Summoner method - apply that return implicit instance
  // 3. Instances
  // 4. Syntax
  trait Semigroup[T] {
    def combine(a: T, b: T): T
  }

  object Semigroup {
    def apply[T](implicit instance: Semigroup[T]): Semigroup[T] = instance
  }

  trait Monoid[T] extends Semigroup[T] {
    def empty: T
  }

  object IntInstances {
    implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
      def empty: Int                   = 0
      def combine(a: Int, b: Int): Int = a + b
    }
  }

  object StringInstances {
    implicit val stringMonoid: Monoid[String] = new Monoid[String] {
      def empty: String                         = ""
      def combine(a: String, b: String): String = a + b
    }
  }

  object SemigroupSyntax {
    implicit class Syntax[T](val a: T) {
      def |+|(b: T)(implicit instance: Semigroup[T]): T = instance.combine(a, b)
    }

//    TODO: what diff between this and above?
//    implicit class Syntax[T: Semigroup](val a: T) {
//      def |+|(b: T): T = combine(a, b)
//    }
  }

  import IntInstances._
  import StringInstances._
  import SemigroupSyntax._

  val meaningOfLive: Int = 40 |+| 2
  println(meaningOfLive)

  val favoriteLanguage: String = Semigroup[String].combine("Scala", " is favorite language")
  println(favoriteLanguage)

  def combine[T](a: T, b: T)(implicit instance: Semigroup[T]): T = instance.combine(a, b)
  combine(10, 20)

  def reduce[T](xs: List[T])(implicit instance: Semigroup[T]): T = xs.reduce(instance.combine)
  reduce(List(1, 2, 3, 4))

  def reduceCompact[T: Semigroup](xs: List[T]): T = xs.reduce(_ |+| _)
  reduce(List("a", "b", "c"))
  // reduce(List('a', 'b', 'c')) // - do not compile >>
  // could not find implicit value for parameter instance:
  // typeclass.SemigroupsAndMonoids.Semigroup[Char]
}
