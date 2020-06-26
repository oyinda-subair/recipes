package com.la.receta

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}
import com.la.receta.config.{ApplicationConfiguration, ApplicationLogger}
import com.la.receta.controller.UserController
import com.la.receta.controller.v1.UserControllerImpl
import com.la.receta.database.{Connector, RecetaDao}
import com.la.receta.route.UserRoute
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object ServiceMain
    extends PlayJsonSupport
    with App
    with ApplicationLogger
    with ApplicationConfiguration {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContext = system.dispatcher

  val recetaDao = {
    val db = Connector.session.db
    new RecetaDao(db)
  }

  val userController: UserController = new UserControllerImpl(recetaDao)

  val userRoute = new UserRoute(userController)

  val routes: Route =
    userRoute.routes

  val binding = Http().bindAndHandle(routes, httpInterface, httpPort)

  binding.onComplete {
    case Success(serverBinding) =>
      log.info(s"==> Server bound to http:/${serverBinding.localAddress}")
    case Failure(ex) =>
      log.error(s"Failed to bind to $httpInterface:$httpPort !", ex)
      system.terminate()
  }
}
