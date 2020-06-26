package com.la.receta.testkit

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.stream.Materializer
import com.la.receta.database.{Connector, SchemaMigration}
import com.la.receta.model.{Members, MembersImpl}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{Json, Reads, Writes}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

trait RecetaTestkit extends PlayJsonSupport {

  lazy val db = Connector.session.db
  val location = Seq("db/migration")
//  val flyway = new SchemaMigration().withMigration(location)
  val memberDb: Members = new MembersImpl(db)

  private val random = new Random(System.currentTimeMillis)

  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))

}
