package com.la.receta.database

import akka.stream.alpakka.slick.scaladsl._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object Connector {
  val databaseConfig = DatabaseConfig.forConfig[JdbcProfile]("slick-mysql")
  implicit val session = SlickSession.forConfig(databaseConfig)
}
