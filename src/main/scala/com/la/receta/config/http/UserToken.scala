package com.la.receta.config.http

import java.util.concurrent.TimeUnit

import com.la.receta.config.{ApplicationConfiguration, Logger}
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.errorhandler.TokenEncodeException
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import play.api.libs.json.{Format, Json}

case class UserToken(token: String)

object UserToken extends Logger with ApplicationConfiguration {

  implicit val format: Format[UserToken] = Json.format

  private val expiryPeriodInDays: Long = 365

  def generate(userId: UserId): UserToken = {
    val token = encodeToken(userId)
    UserToken(token)
  }

  private def encodeToken(userId: UserId): String =
    try {
      val claim = MemberClaim(userId)
      Jwt.encode(setClaims(claim), secret, JwtAlgorithm.HS256)
    } catch {
      case e: Exception =>
        log.error(s"Error encoding token: $e")
        throw TokenEncodeException("Encoding token failed!")
    }

  private def setClaims(claim: MemberClaim): JwtClaim =
    JwtClaim(claim.toString).expiresAt(
      System.currentTimeMillis() + TimeUnit.DAYS
        .toMillis(expiryPeriodInDays)
    )
}
