package com.la.receta.config

import com.typesafe.config.ConfigFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait ApplicationConfiguration {
  private val config = ConfigFactory.load()

  lazy val className: String = if(this.getClass.getCanonicalName != null)
    this.getClass.getCanonicalName else "none"

  val dbUrl: String = sys.env.getOrElse("MYSQL_URL", "jdbc:mysql://localhost:3306/recipes")
  val dbUsername: String = sys.env.getOrElse("MYSQL_USERNAME", "root")
  val dbPassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "password")

  val logger: Logger = LoggerFactory.getLogger(className)

//  val t = 0
//  val oldT = 0
//
//  logger.debug("Temperature set to {}. Old temperature was {}.", t, oldT)
}
