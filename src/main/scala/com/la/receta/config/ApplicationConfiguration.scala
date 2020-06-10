package com.la.receta.config

import com.typesafe.config.ConfigFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait ApplicationConfiguration {
  private val config = ConfigFactory.load()

  lazy val className: String =
    if (this.getClass.getCanonicalName != null)
      this.getClass.getCanonicalName
    else "none"

  val dbUrl: String = sys.env.getOrElse("MYSQL_URL", "")
  val dbUsername: String = sys.env.getOrElse("MYSQL_USERNAME", "")
  val dbPassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "")

  val log: Logger = LoggerFactory.getLogger(className)

  log.debug(s"Connecting to database: $dbUrl")
}
