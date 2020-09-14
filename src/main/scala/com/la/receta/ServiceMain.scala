package com.la.receta

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Directives.{handleExceptions, handleRejections}
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.model.{HttpHeaderRange, HttpOriginMatcher}
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.la.receta.config.http.{JWTConfigImpl, RouteHandler}
import com.la.receta.config.{ApplicationConfiguration, Logger}
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
    with RouteHandler
    with Logger
    with ApplicationConfiguration {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val config: JWTConfigImpl = new JWTConfigImpl

  lazy val recetaDao = {
    val db = Connector.session.db
    new RecetaDao(db)
  }

  val userController: UserController = new UserControllerImpl(recetaDao)

  val userRoute = new UserRoute(userController)

  lazy val routes: Route =
    userRoute.routes

  private val setting = CorsSettings
    .default(system)
    .withAllowedOrigins(HttpOriginMatcher.*)
    .withAllowCredentials(true)
    .withAllowedHeaders(HttpHeaderRange.*)
    .withAllowedMethods(scala.collection.immutable.Seq(POST, PUT, GET, OPTIONS, DELETE))
    .withExposedHeaders(
      scala.collection.immutable.Seq(
        "Content-Type",
        "X-Content-Type",
        "x-access-token",
        "x-refresh-token",
        "Origin",
        "Authorization",
        "X-Requested-With",
        "Set_Cookie"
      )
    )

  val handleErrors = handleRejections(rejectionHandler) & handleExceptions(myExceptionHandler)

  lazy val allRoutes = handleErrors {
    cors(setting) {
      handleErrors {
        routes
      }
    }
  }

  val binding = Http().bindAndHandle(allRoutes, httpInterface, httpPort)

  binding.onComplete {
    case Success(serverBinding) =>
      log.info(s"==> Server bound to http:/${serverBinding.localAddress}")
    case Failure(ex) =>
      log.error(s"Failed to bind to $httpInterface:$httpPort !", ex)
      system.terminate()
  }
}
