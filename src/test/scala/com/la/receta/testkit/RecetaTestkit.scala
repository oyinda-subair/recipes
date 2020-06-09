package com.la.receta.testkit

import com.la.receta.database.{Connector, SchemaMigration}
import com.la.receta.model.MembersImpl

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

trait RecetaTestkit {

  implicit val db = Connector.session.db
  val location = Seq("db/migration")
  val flyway = new SchemaMigration().withMigration(location)
  val memberDb = new MembersImpl()

  private val random = new Random(System.currentTimeMillis)

  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))

}
