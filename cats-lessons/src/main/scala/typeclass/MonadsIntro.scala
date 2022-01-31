package typeclass

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

object MonadsIntro {
  // Should have ability:
  // - to "wrap" value - pure/unit
  // - transform value - bind/flatMap
  final case class SafeValue[+T](private val internalValue: T) {
    def get: T = synchronized {
      internalValue
    }

    def flatMap[S](transformer: T => SafeValue[S]): SafeValue[S] =
      synchronized {
        transformer(internalValue)
      }
  }

  // Suppose we have some external API
  def gimmeSafeValue[T](value: T): SafeValue[T] = SafeValue(value)

  val safeString: SafeValue[String] = gimmeSafeValue("Scala")

  // To process internal String we need extract it from the wrapper
  val scala: String = safeString.get

  // Done, now we want to transform it to upper-case
  val upper: String = scala.toUpperCase

  // And for continue using with "external API" we should back it to the wrapper
  val upperSafeString: SafeValue[String] = SafeValue(upper)

  // ETW pattern - Extract Transform Wrap

  // With transform method we compress extract, transformation and wrap in one operation
  val upperSafeStringTwo: SafeValue[String] = safeString.flatMap(s => SafeValue(s.toUpperCase))

  // Example 1: Census application
  final case class Person(firstName: String, lastName: String) {
    require(firstName != null && lastName != null, "first and last name should not be null ")
  }

  // Census API
  object CensusAPI {
    def getPerson(firstName: String, lastName: String): Person =
      if (firstName != null) {
        if (lastName != null) Person(firstName, lastName)
        else null
      } else null

    def getPersonOption(firstName: String, lastName: String): Option[Person] =
      Option(firstName).flatMap { first =>
        Option(lastName).map { last =>
          Person(first, last)
        }
      }

    def getPersonFor(firstName: String, lastName: String): Option[Person] =
      for {
        first <- Option(firstName)
        last  <- Option(lastName)
      } yield Person(first, last)
  }

  // Example 2: Asynchronous fetches
  final case class User(id: String)
  final case class Product(sku: String, price: Double)

  // Store API
  object Store {
    val testUserURL: String = "my.store.com/users/test-user"

    def getUser(url: String): Future[User] = {
      println(s"getting user by url: $url ...")
      Future(User("test-user"))
    }

    def getLastOrder(userId: String): Future[Product] =
      Future(Product("172-121-21", 7.99))
  }

  // ETW patter
  val priceIncludeOnComplete: Unit =
    Store.getUser(Store.testUserURL).onComplete { case Success(User(id)) =>
      Store.getLastOrder(id).onComplete { case Success(Product(sku, price)) =>
        // Some action with product information
        println(s"Calculation tax for $sku with price: $price")
        price * 1.19
      }
    }

  // flatMap
  val priceIncludeTaxFlatMap: Future[Double] =
    Store.getUser(Store.testUserURL).flatMap { user =>
      Store.getLastOrder(user.id).map { product =>
        println(s"Calculation tax for ${product.sku} with price: ${product.price}")
        product.price * 1.19
      }
    }

  // for-comprehension
  val priceIncludeTaxFor: Future[Double] = for {
    user    <- Store.getUser(Store.testUserURL)
    product <- Store.getLastOrder(user.id)
  } yield {
    println(s"Calculation tax for ${product.sku} with price: ${product.price}")
    product.price * 1.19
  }

  // Example 3: Double-for loops
  val numbers = List(1, 2, 3)
  val chars   = List('a', 'b', 'c')

  // flatMap
  val checkerboardFlatMap: List[(Char, Int)] =
    numbers.flatMap(n => chars.map(ch => ch -> n))

  // for-comprehension
  val checkerboardFor: List[(Char, Int)] =
    for {
      number <- numbers
      char   <- chars
    } yield char -> number

  // Monad Properties
  // Property 1: Monad(x).flatMap(f) == f(x)
  def twoConsecutive(x: Int): List[Int] = List(x, x + 1)
  twoConsecutive(3)               // List(3, 4)
  List(3).flatMap(twoConsecutive) // List(3, 4)

  // Property 2: Right identity (useless wrap)
  // Monad(x).flatMap(y => Monad(y)) - that's useless and returns Monad(x)
  List(1, 2, 3).flatMap(x => List(x)) // List(1, 2, 3)

  // Property 3: Associativity (ETW-ETW)
  val incrementer: Int => List[Int] = x => List(x, x + 1)
  val doubler: Int => List[Int]     = x => List(x, x * 2)

  def main(args: Array[String]): Unit = {
    println(List(1, 2, 3).flatMap(incrementer).flatMap(doubler))
    // List(1, 2, 2, 4,   2, 4, 3, 6,   3, 6, 4, 8)
    // List(
    //    incrementer(1).flatMap(doubler) -> List(1, 2, 2, 4)
    //    incrementer(2).flatMap(doubler) -> List(2, 4, 3, 6)
    //    incrementer(3).flatMap(doubler) -> List(3, 6, 4, 8)
    // )

    println(List(1, 2, 3).flatMap(x => incrementer(x).flatMap(doubler)))
    // List(1, 2, 2, 4,   2, 4, 3, 6,   3, 6, 4, 8)
    // Monad(v).flatMap(f).flatMap(g) == Monad(v).flatMap(x => f(x).flatMap(g))

    println(
      "Is List(1, 2, 3).flatMap(incrementer).flatMap(doubler) == " +
        "List(1, 2, 3).flatMap(x => incrementer(x).flatMap(doubler))? => " + {
          List(1, 2, 3).flatMap(incrementer).flatMap(doubler) ==
            List(1, 2, 3).flatMap(x => incrementer(x).flatMap(doubler))
        }
    )
  }
}
