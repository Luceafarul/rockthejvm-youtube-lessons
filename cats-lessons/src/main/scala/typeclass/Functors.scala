package typeclass

import scala.util.{Success, Try}

object Functors extends App {

  println(List(1, 2, 3).map(_ + 1))
  println(Some(3).map(_ + 4))
  println(Success(72).map(_ + 1))

  def x10Try(t: Try[Int]): Try[Int]            = t.map(_ * 10)
  def x10List(xs: List[Int]): List[Int]        = xs.map(_ * 10)
  def x10Option(opt: Option[Int]): Option[Int] = opt.map(_ * 10)

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object FunctorInstances {
    implicit val listFunctor: Functor[List] = new Functor[List] {
      def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
    }
  }

  object FunctorSyntax {
    implicit class Syntax[F[_], A](value: F[A]) {
      def map[B](f: A => B)(implicit functor: Functor[F]): F[B] = functor.map(value)(f)
    }
  }

  import FunctorSyntax._
  import FunctorInstances._

  def do10x[F[_]: Functor](fa: F[Int]): F[Int] = fa.map(_ * 10)
  println(do10x(List(1, 2, 3)))

  final case class Container[T](value: T)

  object Container {
    implicit val containerFunctor: Functor[Container] = new Functor[Container] {
      def map[A, B](fa: Container[A])(f: A => B): Container[B] = Container(f(fa.value))
    }
  }

  println(do10x(Container(10)))

  sealed trait Tree[+T]
  object Tree {
    def leaf[T](value: T): Tree[T]                                  = Leaf(value)
    def branch[T](value: T, right: Tree[T], left: Tree[T]): Tree[T] = Branch(value, left, right)

    implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
      def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
        case Leaf(value)                => Leaf(f(value))
        case Branch(value, left, right) => Branch(f(value), map(left)(f), map(right)(f))
      }
    }
  }

  final case class Leaf[+T](value: T)                                  extends Tree[T]
  final case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]

  val tree = Tree.branch(5, Tree.leaf(3), Tree.leaf(2))

  println(tree.map(_ * 10))
  println(do10x(tree))
}
