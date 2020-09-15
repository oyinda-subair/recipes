package com.la.receta.testkit

import akka.http.javadsl.model.headers.Authorization
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.http.UserToken
import com.la.receta.controller.UserController
import com.la.receta.database.{Connector, SchemaMigration}
import com.la.receta.mock.MockUserController
import com.la.receta.model.{Members, MembersImpl}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

trait RecetaTestkit extends PlayJsonSupport {

  lazy val db = Connector.session.db
  val location = Seq("db/migration")
//  val flyway = new SchemaMigration().withMigration(location)
  val memberDb: Members = new MembersImpl(db)

  private val random = new Random(System.currentTimeMillis)

  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))

  def generateToken(memberId: UserId): (UserToken, Authorization) = {
    val userToken = UserToken.generate(memberId)
    val header = Authorization.oauth2(userToken.token)
    (userToken, header)
  }

  val mockUserController = new MockUserController()

}
