package com.la.receta.config.errorhandler

import akka.http.javadsl.server.Rejection

case class UnauthorizedUser(message: String) extends Rejection
