package com.la.receta.mock

import com.la.receta.config.DataResponseWrapper
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.base.Util.string10
import com.la.receta.config.http.UserToken
import com.la.receta.controller.UserController
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage, MemberResponse}

import scala.collection.mutable
import scala.concurrent.Future

class MockUserController extends UserController {
  val users = new mutable.HashMap[UserId, MemberResponse]()
  val userToken = new mutable.HashMap[String, UserToken]()

  override def createMember(request: CreateMemberRequest): Future[DataResponseWrapper[UserToken]] =
    Future.successful(DataResponseWrapper(userToken(request.email)))

  override def login(request: LoginRequestMessage): Future[DataResponseWrapper[UserToken]] =
    Future.successful(DataResponseWrapper(userToken(request.email)))

  override def getUserById(userId: UserId): Future[DataResponseWrapper[MemberResponse]] =
    Future.successful(DataResponseWrapper(users(userId)))

  def addToUsers(userId: UserId, memberResponse: MemberResponse): mutable.Map[UserId, MemberResponse] =
    users.addOne(userId, memberResponse)

  def addUserToken(email: String, token: UserToken): mutable.Map[String, UserToken] =
    userToken.addOne(email, token)
}
