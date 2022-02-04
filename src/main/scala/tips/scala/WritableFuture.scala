package tips.scala

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

object WritableFuture extends App {
  val futureInt = Future(42)

  // Futures are inherently non-deterministic

  // Given: Multi-threaded service
  object Service {
    def producePrecisionValue(n: Int): String =
      s"The meaning of life is $n"

    def submitTask[A](a: A)(f: A => Unit): Boolean = {
      // Run the function f on the argument a in some point
      f(a)
      true
    }
  }

  // Target: Get Future[String]
  def precisionValue(n: Int): Future[String] = {
    // Step 1: Create the Promise
    val promise = Promise[String]()

    // Step 5: Call the producer
    Service.submitTask(n) { x: Int =>
      // Step 4: Producer logic
      val precisionValue = Service.producePrecisionValue(x)
      promise.success(precisionValue)
    }

    // Step 2: Extract it's Future
    promise.future
  }

  // Step 3: Someone will consume the Future
  precisionValue(73).foreach(println)

  // If we try this way:
  //  def precisionValue(n: Int): Future[String] = Future {
  //    Service.producePrecisionValue(n)
  //  }
  // Spawning up the thread responsible for evaluating this function
  // is not up to you, it depends on the Service.
  // So, you can't spawn the Future yourself,
  // so you will need to depend on the Service and return of future String.

  // Promises ("controller" of the Future | wrapper over Future)
  // Step 1: Create the Promise
  val promise = Promise[String]()

  // Step 2: Extract it's Future
  val future = promise.future

  // Step 3: Consume the Future
  val processing = future.map(_.toUpperCase)

  // Step 4: Pass the Promise to somewhere else
  def asyncCall(promise: Promise[String]): Unit =
    promise.success("Your value here!")

  // Step 5: Call the producer
  asyncCall(promise)
}
