package com.la.receta.route

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.la.receta.config.http.{AuthenticateDirectives, JWTConfig, JWTConfigImpl}
import com.la.receta.controller.UserController
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.ExecutionContext

class UserRoute(controller: UserController)(implicit
  val config: JWTConfig,
  ec: ExecutionContext
) extends PlayJsonSupport
    with AuthenticateDirectives {

  val version = "v1"
  val service = "members"

  protected val createMember: Route =
    path(service / version / "members") {
      post {
        entity(as[CreateMemberRequest]) { request =>
          complete((StatusCodes.Created, controller.createMember(request)))
        }
      }
    }

  protected val login: Route =
    path(service / version / "login") {
      post {
        entity(as[LoginRequestMessage]) { request =>
          complete((StatusCodes.OK, controller.login(request)))
        }
      }
    }

  protected val myProfile: Route =
    path(service / version / "profile") {
      get {
        authenticated { userId =>
          complete((StatusCodes.OK, controller.getUserById(userId)))
        }
      }
    }

  val routes: Route =
    createMember ~
      login ~
      myProfile

}
