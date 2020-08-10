package com.la.receta.config.errorhandler

case class ResourceNotFoundException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class UnauthorizedUserException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class TokenExpiredException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class TokenEncodeException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class TokenValidationException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class BadRequestException(message: String, cause: Option[Throwable] = None)
  extends Exception(message, cause.orNull)