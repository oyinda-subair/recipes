package com.la.receta.controller

import com.la.receta.config.DataResponseWrapper
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage}

import scala.concurrent.Future

trait UserController {
  def createMember(request: CreateMemberRequest): Future[DataResponseWrapper[String]]
  def login(request: LoginRequestMessage): Future[DataResponseWrapper[String]]
}
