package com.la.receta.config.errorhandler

import akka.http.scaladsl.server.Rejection

case class UnauthorizedUser(message: String) extends Rejection
