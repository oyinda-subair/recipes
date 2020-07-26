package com.la.receta.route

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.la.receta.config.DataResponseWrapper
import com.la.receta.controller.UserController
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage}
import com.la.receta.testkit.{RecetaRouteTestkit, RecetaTestkit}
import org.mockito.{Mockito, MockitoSugar}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

class UserRouteSpec
    extends AnyWordSpec
    with Matchers
    with ScalatestRouteTest
    with MockitoSugar
    with RecetaRouteTestkit {
  val userController: UserController = mock[UserController]
  val userRoute = new UserRoute(userController)
  val routes: Route = userRoute.routes
  val version: String = userRoute.version
  val service: String = userRoute.service

  "UserRoute Spec" should {
    "create new user" in {
      val request = CreateMemberRequest(string10, string10, s"$string10.email.com", "password")
      val response = Future.successful(DataResponseWrapper("userId1"))

      when(userController.createMember(request)).thenReturn(response)

      Post(s"/$service/$version/members").withEntity(toEntity(request)) ~> routes ~> check {
        status shouldEqual StatusCodes.Created
      }

      verify(userController, times(1)).createMember(request)
    }

    "Login user" in {
      val request = LoginRequestMessage(string10, string10)
      val response = Future.successful(DataResponseWrapper("userId1"))

      when(userController.login(request)).thenReturn(response)

      Post(s"/$service/$version/login").withEntity(toEntity(request)) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }

      verify(userController, times(1)).login(request)
    }
  }
}
