package tips.scala

object NothingExample extends App {
  class SomeClass // implicit extends AnyRef

  // Why we need Nothing type?

  // As we see - NoSuchElementException not confused compiler
  // in methods which have return type as Int, String, SomeClass
  def getNumber: Int     = throw new NoSuchElementException
  def getString: String  = throw new NoSuchElementException
  def getSome: SomeClass = throw new NoSuchElementException

  // throw expression return... NOTHING! It doesn't have any values.
  // Nothing != Unit
  // Nothing != Null
  // Nothing != anything at all
  // Nothing is the type of "nothingness"
  // Nothing is valid sub-type of any other types

  // Can we use Nothing?
  def functionWithNothingParam(nothing: Nothing): Int     = 73
  def functionWithNothingParam2(nothing: => Nothing): Int = 73

  // It was fail, because param evaluate before step into inside method
  // functionWithNothingParam(throw new NullPointerException)
  println(functionWithNothingParam2(throw new NullPointerException))

  def functionReturningNothing: Nothing = throw new IllegalArgumentException

  // Nothing is useful in GENERICS, in COVARIANT [+A] GENERICS

  // If        Dog    extends        Animal,
  // then List[Dog]      <:     List[Animal]
  abstract class List[+T]
  final case class NonEmptyList[+T](head: T, tail: List[T])
  object EmptyList extends List[Nothing]

  val listOfStrings: List[String]      = EmptyList
  val listOfDoubles: List[Double]      = EmptyList
  val listOfSomeClass: List[SomeClass] = EmptyList

  // ??? return Nothing
  def unimplementedMethod: String = ???

  // And go check what is that ???
  /** `???` can be used for marking methods that remain to be implemented.
    * @throws NotImplementedError
    *   when `???` is invoked.
    * @group utilities
    */
  // def ??? : Nothing = throw new NotImplementedError
}
