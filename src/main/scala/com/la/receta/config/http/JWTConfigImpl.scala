package com.la.receta.config.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.{ApplicationConfiguration, Logger}
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import play.api.libs.json.Json

import scala.util.{Success, Try}

trait JWTConfig {
  def isTokenValid(jwt: String): Boolean
  def getMemberId(jwt: String): Directive1[UserId]
}

class JWTConfigImpl extends ApplicationConfiguration with Logger with JWTConfig {
  val secretKey = secret
  val header = Seq(JwtAlgorithm.HS256)

  def isTokenValid(jwt: String): Boolean =
    Jwt.isValid(jwt, secretKey, header)

  private def getClaims(jwt: String) =
    decodeToken(jwt) match {
      case Success(claim) => Json.parse(claim.content).asOpt[MemberClaim]
      case _ =>
        log.error("Error decoding token")
        None
    }

  private def decodeToken(token: String): Try[JwtClaim] =
    Jwt.decode(token, secretKey, header)

  def getMemberId(jwt: String): Directive1[UserId] =
    getClaims(jwt) match {
      case Some(value) => provide(value.userId)
      case None =>
        log.error("Invalid authorization credential type")
        reject(AuthorizationFailedRejection)
    }

  def validateToken(jwt: String) = Jwt.validate(jwt, secretKey, header)
}
