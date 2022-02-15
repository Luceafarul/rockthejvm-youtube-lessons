package tips.scala

object ExpressivenessTricks extends App {
  // 1. The single abstract method pattern (SAM). Is this good or not?
  trait Action {
    def action(x: Int): Int
  }

  val increment: Action = (x: Int) => x + 1

  println(increment.action(1))

  // You can use this
  new Thread(new Runnable {
    override def run(): Unit = println("I'm running")
  })

  // or that
  new Thread(() => println("I'm running too"))

  // 2. Right-associative methods
  val prependElements = 1 :: 2 :: List(3)

  val isTrue   = 1 :: 2 :: 3 :: Nil == 1 :: (2 :: (3 :: Nil))
  val alsoTrue = 1 :: (2 :: (3 :: Nil)) == Nil.::(3).::(2).::(1)
  println(isTrue == alsoTrue)

  final case class MessageQueue[T](content: List[T]) {
    def ->:(value: T): MessageQueue[T] = MessageQueue[T](content :+ value)
  }

  val queue = 3 ->: 2 ->: 1 ->: MessageQueue(List.empty)
  println(queue)

  // 3. Backed-in "setters"
  class MutableIntWrapper {
    private var internalValue = 0

    def value: Int                = internalValue
    def value_=(value: Int): Unit = internalValue = value

    override def toString: String = s"MutableIntWrapper($value)"
  }
  object MutableIntWrapper {
    def apply(n: Int): MutableIntWrapper = {
      val wrapper = new MutableIntWrapper()
      wrapper.value = n // here compiler call wrapper.value_=(n)
      wrapper
    }
  }

  val wrapper = MutableIntWrapper(3)
  println(wrapper)
  wrapper.value = 13
  println(wrapper)

  // 4. Multi-word members (using in some libs, like akka-http for content types
  final case class Person(name: String) {
    def `then said`(msg: String): Unit = println(s"$name said: $msg")
  }

  val jim = Person("Jim")
  jim `then said` "omg!? this is name for method?"

  // 5. Back-ticks for Pattern Matching
  val numberOne      = 13
  val numberTwo: Any = 73

  numberTwo match {
    case n if n == numberOne => println(n)
    case _                   => println(s"$numberTwo not equal to $numberOne")
  }

  numberTwo match {
    case `numberOne` => println(numberOne)
    case _           => println(s"$numberTwo not equal to $numberOne")
  }
}
