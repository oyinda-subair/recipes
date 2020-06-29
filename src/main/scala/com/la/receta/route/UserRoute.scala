package com.la.receta.route

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.la.receta.controller.UserController
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.ExecutionContext

class UserRoute(controller: UserController)(implicit
  val system: ActorSystem,
  materializer: Materializer,
  ec: ExecutionContext
) extends PlayJsonSupport {

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

  val routes: Route =
    createMember ~
      login

}
