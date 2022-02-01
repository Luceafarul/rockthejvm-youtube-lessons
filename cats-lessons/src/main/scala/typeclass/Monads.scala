package typeclass

import scala.util.{Failure, Success, Try}

object Monads {

  val list                       = List(1, 2, 3, 4)
  val incrementedList: List[Int] = list.map(_ + 1)
  val flatMappedList: List[Int]  = list.flatMap(x => List(x, x + 1))

  def combineLists(xs: List[String])(ns: List[Int]): List[(String, Int)] =
    for {
      s <- xs
      n <- ns
    } yield (s, n)

  def combineOptions(os: Option[String])(on: Option[Int]): Option[(String, Int)] =
    for {
      s <- os
      n <- on
    } yield (s, n)

  def combineTry(ts: Try[String])(tn: Try[Int]): Try[(String, Int)] =
    for {
      s <- ts
      n <- tn
    } yield (s, n)

  trait Monad[M[_]] {
    def pure[A](a: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    // method map from Functor
    // Monad naturally extends Functor
    // we get map for free via flatMap
    def map[A, B](ma: M[A])(f: A => B): M[B] =
      flatMap(ma)(a => pure(f(a)))
  }

  object Monad {
    def apply[M[_]](implicit monad: Monad[M]): Monad[M] = monad
  }

  object MonadSyntax {
    implicit class Syntax[M[_]: Monad, A](ma: M[A]) {
      def pure(a: A): M[A]               = Monad[M].pure(a)
      def flatMap[B](f: A => M[B]): M[B] = Monad[M].flatMap(ma)(f)
      def map[B](f: A => B): M[B]        = Monad[M].map(ma)(f)
    }
  }

  object MonadInstances {
    implicit val monadList: Monad[List] = new Monad[List] {
      def pure[A](a: A): List[A]                               = List(a)
      def flatMap[A, B](ma: List[A])(f: A => List[B]): List[B] = ma.flatMap(f)
    }
  }

  def combine[M[_]](ms: M[String])(mn: M[Int])(implicit monad: Monad[M]): M[(String, Int)] =
    monad.flatMap(ms)(s => monad.map(mn)(n => (s, n)))

  // For using for-comprehension we need syntax for Monad
  // with syntax we can use: ms.flatMap(s => mn.map(n => (s, n)))
  // or for-comprehension
  import MonadSyntax._
  def combine2[M[_]: Monad](ms: M[String])(mn: M[Int]): M[(String, Int)] =
    for {
      s <- ms
      n <- mn
    } yield (s, n)

  def main(args: Array[String]): Unit = {
    import Monads.MonadInstances._

    println(combineLists(List("a", "b", "c"))(List(1, 2, 3, 4)))
    println(combineOptions(Option("A"))(Option(1)))
    println(combineTry(Success("A"))(Failure(new IllegalArgumentException("failed number"))))

    println(combine(List("a", "b", "c"))(List(1, 2, 3, 4)))
    println(combine2(List("a", "b", "c"))(List(1, 2, 3, 4)))
  }
}
