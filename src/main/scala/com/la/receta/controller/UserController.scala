package com.la.receta.controller

import com.la.receta.config.DataResponseWrapper
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.http.UserToken
import com.la.receta.entities._

import scala.concurrent.Future

trait UserController {
  def createMember(request: CreateMemberRequest): Future[DataResponseWrapper[UserToken]]
  def login(request: LoginRequestMessage): Future[DataResponseWrapper[UserToken]]
  def getUserById(userId: UserId): Future[DataResponseWrapper[MemberResponse]]
}
