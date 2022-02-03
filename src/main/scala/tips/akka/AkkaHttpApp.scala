package tips.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.{model, Http}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethod, HttpMethods, HttpRequest, HttpResponse}

import java.net.URLEncoder
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object AkkaHttpApp extends App {
  implicit val system: ActorSystem = ActorSystem()

  import system.dispatcher

  val source =
    """
      |object SimpleApp extends App {
      |  val field: Int = 2
      |
      |  def method(x: Int): Int = x + 1
      |}
      |""".stripMargin

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://markup.su/api/highlighter",
    entity = HttpEntity(
      ContentTypes.`application/x-www-form-urlencoded`,
      s"source=${URLEncoder.encode(source.trim, "UTF-8")}&language=Scala&theme=Sunburst"
    )
  )

  def sendRequest(req: HttpRequest): Future[String] =
    for {
      resp   <- Http().singleRequest(req)
      entity <- resp.entity.toStrict(2.seconds)
    } yield entity.data.utf8String

  sendRequest(request).foreach(println)
}
