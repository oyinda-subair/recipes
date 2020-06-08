package com.la.receta.config

import com.typesafe.config.ConfigFactory

trait ApplicationConfiguration {
  private val config = ConfigFactory.load()

  lazy val className: String = if(this.getClass.getCanonicalName != null)
    this.getClass.getCanonicalName else "none"

  val dbUrl: String = sys.env.getOrElse("MYSQL_URL", "jdbc:mysql://localhost:3306/recipes")
  val dbUsername: String = sys.env.getOrElse("MYSQL_USERNAME", "root")
  val dbPassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "password")
}
