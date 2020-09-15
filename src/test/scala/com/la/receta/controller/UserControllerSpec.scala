package com.la.receta.controller

import java.util.UUID

import akka.http.javadsl.model.headers.Authorization
import com.la.receta.config.http.UserToken
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage, MemberResponse}
import com.la.receta.model.Member
import com.la.receta.testkit.RecetaTestkit
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UserControllerSpec extends AnyWordSpec
  with Matchers
  with ScalaFutures
  with IntegrationPatience
  with RecetaTestkit {

  val request: CreateMemberRequest = CreateMemberRequest(s"Name $string10", s"username-$string10", s"$string10@example.com", string10)
  val user: Member =
    Member.create(request.name, request.username, request.email, request.password)
  val token: (UserToken, Authorization) = generateToken(user.userId)
  val request1: CreateMemberRequest = CreateMemberRequest(s"Name $string10", s"username-$string10", s"$string10@example.com", string10)
  val user1: Member =
    Member.create(request1.name, request1.username, request1.email, request1.password)
  val token1: (UserToken, Authorization) = generateToken(user.userId)

  mockUserController.addUserToken(request.email, token._1)
  mockUserController.addUserToken(request1.email, token1._1)
  mockUserController.addToUsers(user.userId, MemberResponse(user.userId, user.name, user.username, Some(user.email)))
  mockUserController.addToUsers(user1.userId, MemberResponse(user1.userId, user1.name, user1.username, Some(user1.email)))

  "User Controller" should {
    "create new member" in {

      val response = mockUserController.createMember(request).futureValue

      val content = response.data
      content.token shouldEqual token._1.token
    }

    "login user" in {
      val login = LoginRequestMessage(request.email, request.password)
      val response = mockUserController.login(login).futureValue

      val content = response.data
      content.token shouldEqual token._1.token
    }

    "get user profile" in {
      val response = mockUserController.getUserById(user1.userId).futureValue
      val content = response.data
      content.name shouldEqual user1.name
    }
  }

}
