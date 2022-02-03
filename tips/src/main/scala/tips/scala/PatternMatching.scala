package tips.scala

object PatternMatching {
  // 1. Switch on steroids
  val n = 44
  val ordinal: String = n match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => n + "th"
  }

  // 2. Case class deconstruction
  final case class Person(name: String, age: Int)
  val bob: Person = Person("Bob", 73)

  def greeting(p: Person): String =
    p match {
      case Person(n, a) => s"Hi, my name is $n and I'm $a years old"
    }

  // Tricks
  // 1. List extractors
  val list = List(1, 2, 3, 4, 5)

  val mustHaveThree: String = list match {
    case List(_, _, 3, other) => s"List has 3rd element 3 with $other elements"
    case _                    => "List has not 3rd element 3"
  }

  // 2. List prepending
  val startsWithOne: String = list match {
    case 1 :: _ => "List starts with one"
    case _      => "List does not starts with one"
  }

  // 3. Vararg pattern
  val doNotCareAboutOther: String = list match {
    case List(_, 2, _*) => s"List has 2nd element 2"
    case _              => "List has not 2nd element 2"
  }

  // 4. Other infix pattern
  val mustEndWithFive: String = list match {
    case List(_*) :+ 5 => "List end with 5"
    case _             => "List does not end with 5"
  }

  // 5. Type specifiers
  val someValue: Any = Person("Yaroslav", 29)

  val whatIsType: String = someValue match {
    case _: String => "It is String type"
    case _: Int    => "It is Int type"
    case _: Seq[_] => "It is Seq type"
    case _: Person => "It is Person type"
    case _         => s"I don't know what is type of $someValue"
  }

  // 6. Binding
  def infoRequest(p: Person): String = s"Person ${p.name} is a good person!"
  val bobInfo: String = bob match {
    case p @ Person(name, _) => s"$name's info: ${infoRequest(p)}"
    case _                   => s"$bob is not a person"
  }

  // 7. Conditional guards
  val betterOrdinal: String = n match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case n if n % 10 == 1 => n + "st "
    case n if n % 10 == 2 => n + "nd"
    case n if n % 10 == 3 => n + "rd"
    case _ => n + "th"
  }

  // 8. Alternative patterns
  val optimalList: String = list match {
    case List(1, _*) | List(_, _, 3, _*) => "It's a good list"
    case _                               => "It's a not enough good list"
  }

  def main(args: Array[String]): Unit =
    println(optimalList)
}
