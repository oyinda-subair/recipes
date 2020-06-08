package com.la.receta.testkit

import com.la.receta.database.{Connector, SchemaMigration}
import com.la.receta.model.MembersImpl

import scala.concurrent.ExecutionContext.Implicits.global

trait RecetaTestkit {

  implicit val db = Connector.session.db
  val location = Seq("filesystem:modules/recipes/src/test/resources/db/migration")
  val flyway = new SchemaMigration().withMigration(location)
  val memberDb = new MembersImpl()
}
