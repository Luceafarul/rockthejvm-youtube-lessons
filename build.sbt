import Dependencies._

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  apacheCommonsIo,
  akkaHttp,
  akkaStream,
  akkaHttpSprayJson,
  scalaTest % Test
)
