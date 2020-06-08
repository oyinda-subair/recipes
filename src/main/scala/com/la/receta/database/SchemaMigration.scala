package com.la.receta.database

import com.la.receta.config.ApplicationConfiguration
import org.flywaydb.core.Flyway
import slick.jdbc.hikaricp.HikariCPJdbcDataSource
import slick.jdbc.{DataSourceJdbcDataSource, JdbcProfile}

import scala.util.control.NonFatal

class SchemaMigration(implicit val db: JdbcProfile#Backend#Database) extends ApplicationConfiguration {
  def withMigration(dir: Seq[String], schema: String = "recipes", baseline: Boolean = false): JdbcProfile#Backend#Database = {
    val flyway = Flyway.configure()
    val ds = db.source match {
      case d: DataSourceJdbcDataSource =>
        d.ds
      case d: HikariCPJdbcDataSource =>
        d.ds
      case other =>
        throw new IllegalStateException("Unknown DataSource type: " + other)
    }

    flyway.dataSource(ds)
    flyway.locations(dir: _*)
//    flyway.baselineOnMigrate(baseline)
//    flyway.schemas(schema)

    try {

      flyway.load().migrate()
    } catch {
      case NonFatal(ex) =>
        ex.printStackTrace()
        db.shutdown
    }

    db
  }
}
