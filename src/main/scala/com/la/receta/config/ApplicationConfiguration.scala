package com.la.receta.config

import com.typesafe.config.ConfigFactory

trait ApplicationConfiguration {
  private val config = ConfigFactory.load()

  val dbUrl: String = sys.env.getOrElse("MYSQL_URL", "")
  val dbUsername: String = sys.env.getOrElse("MYSQL_USERNAME", "")
  val dbPassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "")

  val secret: String = sys.env.getOrElse("SECRET", "")

  private val app = config.getConfig("app")

  val httpInterface: String = app.getString("host")
  val httpPort: Int = app.getInt("port")

}
