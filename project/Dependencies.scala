import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"
  lazy val catsCore  = "org.typelevel" %% "cats-core" % "2.3.0"

  val akkaVersion     = "2.6.8"
  val akkaHttpVersion = "10.2.7"

  // akka streams
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  // akka http
  lazy val akkaHttp          = "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
}
