name := "recipes"

val AkkaStreamVersion = "2.6.4"
val AkkaHttVersion = "10.1.12"
val SlickVersion = "2.0.0"
val MySqlJavaVersion = "8.0.20"
val JodaTimeVersion = "2.10.6"
val JodaConvertVersion = "2.2.1"
val SlickJodaMapperVersion = "2.4.2"
val ScalaTestVersion = "3.1.2"
val MockitoVersion = "3.3.3"
val MockitoScalaTestVersion = "1.14.4"
val FlywayVersion = "6.4.3"
val slf4jVersion = "1.7.30"
val qosVersion = "1.2.3"
val GsonVersion = "2.8.6"
val PlayJsonVersion = "2.9.0"
val PlayJsonSupportVersion = "1.32.0"
val ScalaFmtVersion = "2.5.3"
val BcyrptVersion = "0.4"
val AkkaHttpCorsVersion = "1.0.0"
val PauldijouVersion = "4.2.0"



val akkaSlick  = "com.lightbend.akka" %% "akka-stream-alpakka-slick" % SlickVersion
val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttVersion
val akkaHttpCors = "ch.megard" %% "akka-http-cors" % AkkaHttpCorsVersion

val mysqlJava  = "mysql" % "mysql-connector-java" % MySqlJavaVersion

val jodaTime   = "joda-time" % "joda-time" % JodaTimeVersion
val jodaConvert = "org.joda" % "joda-convert" % JodaConvertVersion
val tototoshi  = "com.github.tototoshi" %% "slick-joda-mapper" % SlickJodaMapperVersion

val akkaStreamTest = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaStreamVersion
val akkaTest = "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttVersion

val scalaTest = "org.scalatest" % "scalatest_2.13" % ScalaTestVersion % "test"
val mockito = "org.mockito" % "mockito-core" % MockitoVersion % Test
val mockitoScalaTest = "org.mockito" %% "mockito-scala-scalatest" % MockitoScalaTestVersion % Test
val flywayCore = "org.flywaydb" % "flyway-core" % FlywayVersion

val slf4j      = "org.slf4j" % "slf4j-api" % slf4jVersion
val qos            = "ch.qos.logback" % "logback-classic" % qosVersion
val qosCore       = "ch.qos.logback" % "logback-core" % qosVersion

val scalaFmt = "org.scalameta" %% "scalafmt-cli" % ScalaFmtVersion

val gson = "com.google.code.gson" % "gson" % GsonVersion
val playJson = "com.typesafe.play" %% "play-json" % PlayJsonVersion
val playJsonSupport = "de.heikoseeberger" %% "akka-http-play-json" % PlayJsonSupportVersion

val bcyrpt   = "org.mindrot" % "jbcrypt" % BcyrptVersion
val pauldijou = "com.pauldijou" %% "jwt-core" % PauldijouVersion


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
  .enablePlugins(FlywayPlugin)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      akkaStream,
      akkaSlick,
      akkaHttp,
      akkaHttpCors,
      jodaTime,
      jodaConvert,
      tototoshi,
      mysqlJava,
      gson,
      playJson,
      playJsonSupport,
      slf4j,
      qos,
      qosCore,
      akkaStreamTest,
      akkaTest,
      scalaTest,
      mockito,
      mockitoScalaTest,
      flywayCore,
      bcyrpt,
      pauldijou
    )
  )

flywayUrl := sys.env.getOrElse("MYSQL_URL", "some default value")
flywayUser := sys.env.getOrElse("MYSQL_USERNAME", "some default value")
flywayPassword := sys.env.getOrElse("MYSQL_PASSWORD", "some default value")
flywayLocations += "db/migration"
flywayUrl in Test := sys.env.getOrElse("MYSQL_URL", "some default value")
flywayUser in Test := sys.env.getOrElse("MYSQL_USERNAME", "root")
flywayPassword in Test := sys.env.getOrElse("MYSQL_PASSWORD", "password")
flywayBaselineOnMigrate := true

addCommandAlias("style", "Compile/scalafmt; Test/scalafmt; scalafmtSbt")
addCommandAlias("styleCheck", "Compile/scalafmtCheck; Test/scalafmtCheck; scalafmtSbtCheck")