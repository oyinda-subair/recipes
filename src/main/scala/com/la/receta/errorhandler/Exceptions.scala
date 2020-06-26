package com.la.receta.errorhandler

case class ResourceNotFoundException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)

case class UnauthorizedUserException(message: String, cause: Option[Throwable] = None)
    extends Exception(message, cause.orNull)
