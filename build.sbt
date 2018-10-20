name := "IntroPlay"

version := "2.6.20"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.4"

libraryDependencies += specs2 % Test
