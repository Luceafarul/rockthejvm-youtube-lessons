import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"

lazy val `cats-lessons` = (project in file("cats-lessons")).settings(
  libraryDependencies ++= Seq(
    scalaTest % Test
  )
)
