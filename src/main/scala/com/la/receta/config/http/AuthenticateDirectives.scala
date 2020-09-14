package com.la.receta.config.http

import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive1}
import akka.http.scaladsl.server.Directives.{extractCredentials, provide, reject}
import com.la.receta.config.Logger
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.errorhandler.TokenExpiredException

trait AuthenticateDirectives extends Logger {
  implicit val config: JWTConfig

  def authenticated: Directive1[UserId] =
    extractCredentials.flatMap {
      case Some(OAuth2BearerToken(jwt)) if !config.isTokenValid(jwt) =>
        log.error("User token expired!")
        throw TokenExpiredException("Token expired!")

      case Some(OAuth2BearerToken(jwt)) if config.isTokenValid(jwt) =>
        config.getMemberId(jwt)

      case _ =>
        log.error("Unauthorized user: authentication failed")
        reject(AuthorizationFailedRejection)
    }
}
