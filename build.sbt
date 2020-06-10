name := "recipes"

val AkkaVersion = "2.5.31"
val SlickVersion = "2.0.0"
val MySqlJavaVersion = "8.0.20"
val jodaTimeVersion = "2.10.6"
val jodaConvertVersion = "2.2.1"
val slickJodaMapperVersion = "2.4.2"
val scalaTestVersion = "3.1.2"
val flywayVersion = "6.4.3"
val slf4jVersion = "1.7.30"
val qosVersion = "1.2.3"



val akkaSlick  = "com.lightbend.akka" %% "akka-stream-alpakka-slick" % SlickVersion
val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion

val mysqlJava  = "mysql" % "mysql-connector-java" % MySqlJavaVersion

val jodaTime   = "joda-time" % "joda-time" % jodaTimeVersion
val jodaConvert = "org.joda" % "joda-convert" % jodaConvertVersion
val tototoshi  = "com.github.tototoshi" %% "slick-joda-mapper" % slickJodaMapperVersion

val scalaTest = "org.scalatest" % "scalatest_2.13" % scalaTestVersion % "test"
val flywayCore = "org.flywaydb" % "flyway-core" % flywayVersion
val slickMigrationAPI = "com.1on1development" %% "slick-migration-api-flyway" % "0.4.1"

val slf4j      = "org.slf4j" % "slf4j-api" % slf4jVersion
val qos            = "ch.qos.logback" % "logback-classic" % qosVersion
val qosCore       = "ch.qos.logback" % "logback-core" % qosVersion

val scalaFmt = "org.scalameta" %% "scalafmt-cli" % "2.5.3"

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.13.2",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings"
  ),
  resolvers ++= Seq(
    "typesafe" at "https://repo.typesafe.com/typesafe/releases/",
    Resolver.jcenterRepo,
    "Artima Maven Repository" at "https://repo.artima.com/releases",
    "Flyway" at "https://flywaydb.org/repo"
  )
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      akkaStream,
      akkaSlick,
      jodaTime,
      jodaConvert,
      tototoshi,
      mysqlJava,
      slf4j,
      qos,
      qosCore,
      scalaTest,
      flywayCore
    )
  )