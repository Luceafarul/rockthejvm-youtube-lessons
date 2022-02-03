import Dependencies._

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  akkaHttp,
  akkaStream,
  akkaHttpSprayJson,
  scalaTest % Test,
)
