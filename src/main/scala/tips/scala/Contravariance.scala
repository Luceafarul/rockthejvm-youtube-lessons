package tips.scala

object Contravariance {
  // Let's look on list definition:
  // type List[+A] = scala.collection.immutable.List[A]
  // sealed abstract class List[+A]
  //  extends AbstractSeq[A] with ...
  // What is that +A?
  val list: List[Int] = List(1, 2, 3)

  class Animal
  class Dog(val name: String) extends Animal

  // Question (the variance question):
  // If Dog <: Animal, does List[Dog] <: List[Animal]?
  //        <: - Subtype

  // If answer YES - then the type calls COVARIANT [+A]

  val d1 = new Dog("Lessie")
  val d2 = new Dog("Jessie")
  val d3 = new Dog("Perky")

  val animal: Animal = d1

  val dogs: List[Animal] = List(d1, d2, d3) // List of dogs it's a list of animals

  // If answer NO - then the type calls INVARIANT [A]
  class InvariantList[T]

  // This code does not compile
  // val invariantList: InvariantList[Animal] = new InvariantList[Dog]
  val invariantList: InvariantList[Animal] = new InvariantList[Animal]

  // with error:
  // type mismatch;
  //    found   : tips.scala.Contravariance.InvariantList[Dog]
  //    required: tips.scala.Contravariance.InvariantList[Animal]
  // Note: tips.scala.Contravariance.Dog <: tips.scala.Contravariance.Animal,
  //       but class InvariantList is invariant in type T.
  // You may wish to define T as +T instead. (SLS 4.5)
  //    val invariantList: InvariantList[Animal] = new InvariantList[Dog]

  // If answer HELL NO, QUITE THE OPPOSITE - then the type calls CONTRAVARIANCE [-A]
  class ContravariantList[-T]

  val contravariantList0: ContravariantList[Dog]    = new ContravariantList[Animal]
  val contravariantList2: ContravariantList[Animal] = new ContravariantList[Animal]
  // This code does not compile
  // val contravariantList3: ContravariantList[Animal] = new ContravariantList[Dog]

  // with error:
  // type mismatch;
  // found   : tips.scala.Contravariance.ContravariantList[Dog]
  // required: tips.scala.Contravariance.ContravariantList[Animal]
  //   val contravariantList3: ContravariantList[Animal] = new ContravariantList[Dog]

  // But... why we need contravariance?
  trait Vet[-T] {
    def heal(animal: Animal): Boolean
  }

  def callVet: Vet[Animal] = new Vet[Animal] {
    def heal(animal: Animal): Boolean = {
      println("You will be fine!")
      true
    }
  }

  val buddy              = new Dog("Buddy")
  val buddyVet: Vet[Dog] = callVet

  // Also, let's look on Function trait:
  // trait Function1[-T1, +R] extends AnyRef { self => ... }
  // it's CONTRAVARIANCE [-T1] in first type argument - incoming parameter and
  //      COVARIANT       [+R] in second type argument - produced (returned) value
  // if we have function f: Dog => String,
  // we can pass as input Dog or any super-type of Dog and
  // return String or any subtype of String
  // TODO: and what...???

  // Rule of thumb:
  // - if your generic types CONTAINS or CREATES elements of type T, it should be +T
  // - if your generic types ACTS or CONSUMES elements of type T, it should be -T

  def main(args: Array[String]): Unit =
    buddyVet.heal(buddy)
}
