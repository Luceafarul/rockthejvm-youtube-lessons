package tips.scala

object CustomInterpolation extends App {
  // s-interpolator
  val pi = math.Pi
  println(s"This is s-interpolation string: $pi")

  // raw-interpolator
  println(raw"This is raw-interpolation string: \n <- this is not new line")

  // f-interpolator
  println(f"This is f-interpolation string: $pi%.2f")

  // Custom interpolator
  // like person"name,age" -> Person
  final case class Person(name: String, age: Int)

  // simple approach
  def fromStringToPerson(s: String): Person = {
    val Array(name, age) = s.split(",")
    Person(name, age.toInt)
  }

  println(s"""fromStringToPerson("Yaroslav,29") -> ${fromStringToPerson("Yaroslav,29")}""")

  // interpolator approach
  // 1. Create implicit class that accept StringContext as c-tor argume t
  // 2. Add method that names using for interpolator
  // 3. Implement method
  implicit class PersonInterpolator(sc: StringContext) {
    def person(args: Any*): Person = {
      val totalString = sc.s(args: _*)

      val Array(name, age) = totalString.split(",")
      Person(name, age.toInt)
    }
  }

  print("person\"Yaroslav,29\" -> ")
  val name = "Yaroslav"
  val age = 29
  println(person"$name,$age")
}
