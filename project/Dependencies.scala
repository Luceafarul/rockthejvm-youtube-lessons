import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"
  lazy val catsCore  = "org.typelevel" %% "cats-core" % "2.3.0"

  private val akkaVersion     = "2.6.18"
  private val akkaHttpVersion = "10.2.7"

  // apache commons
  lazy val apacheCommonsIo = "commons-io" % "commons-io" % "2.6"
  // akka actors
  lazy val akkaActorTyped        = "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion
  lazy val akkaActorTestkitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test
  // akka streams
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  // akka http
  lazy val akkaHttp          = "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
}
