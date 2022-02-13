package tips.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import java.time.LocalTime
import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

object AsyncNonBlocking extends App {
  def infoLog(msg: String): Unit =
    println(s"[${Thread.currentThread().getName}:${LocalTime.now}] $msg...")

  // Synchronous blocking call
  infoLog(s"Before blocking call...")
  def blockingFunction(x: Int): Int = {
    Thread.sleep(5000)
    x + 73
  }

  blockingFunction(13) // blocking call
  val valueOne = {
    infoLog("Waiting...")
    15
  } // will wait 5 seconds before evaluating it

  // Asynchronous blocking call
  infoLog(s"Before async blocking call...")
  def asyncBlockingFunction(x: Int): Future[Int] = Future {
    Thread.sleep(5000)
    x + 73
  }

  asyncBlockingFunction(13) // async blocking call
  val valueTwo = {
    infoLog(s"Non waiting...")
    15
  } // evaluate it immediately

  // Asynchronous non-blocking call
  def createSimpleActor: Behaviors.Receive[String] =
    Behaviors.receiveMessage[String] { msg =>
      infoLog(s"Received message: $msg")
      Behaviors.same
    }

  val rootActor = ActorSystem(createSimpleActor, "test-system")

  rootActor ! "test message" // enqueuing a message, async non-blocking call
  val valueThree = {
    infoLog(s"Non waiting too...")
    15
  } // evaluate it immediately

  val promiseResolver = ActorSystem(
    Behaviors.receiveMessage[(String, Promise[Int])] { case (msg, promise) =>
      promise.success(msg.length)
      Behaviors.same
    },
    "promise-resolver"
  )

  def asyncNonBlocking(s: String): Future[Int] = {
    val promise = Promise[Int]()
    promiseResolver ! (s, promise)
    promise.future
  }

  // Future[Int] - async, non-blocking
  val asyncNonBlockingResult = asyncNonBlocking("one more test message")
  asyncNonBlockingResult.onComplete(res => infoLog(res.toString))
}
