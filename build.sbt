name := "recipes"

version := "0.1"

scalaVersion := "2.13.2"

val AkkaVersion = "2.5.31"
val SlickVersion = "2.0.0"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % SlickVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)