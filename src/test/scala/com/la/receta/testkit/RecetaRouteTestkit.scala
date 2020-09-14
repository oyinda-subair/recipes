package com.la.receta.testkit

import java.util.UUID

import akka.http.javadsl.model.headers.Authorization
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directive1
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.http.{JWTConfig, UserToken}
import play.api.libs.json.{Json, Reads, Writes}

import scala.util.Random

trait RecetaRouteTestkit {
  private val random = new Random(System.currentTimeMillis)

  val userId: UserId = UUID.randomUUID().toString

  def toEntity[T: Reads: Writes](body: T): HttpEntity.Strict = {
    val message = Json.toJson(body).toString()
    HttpEntity(MediaTypes.`application/json`, message)
  }

  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))

  implicit val jWTConfig: JWTConfig = new JWTConfig {
    override def isTokenValid(jwt: String): Boolean = true

    override def getMemberId(jwt: String): Directive1[UserId] = provide(userId)
  }

  def generateToken(memberId: UserId) = {
    val userToken = UserToken.generate(memberId)
    val header = Authorization.oauth2(userToken.token)
    (userToken, header)
  }
}
