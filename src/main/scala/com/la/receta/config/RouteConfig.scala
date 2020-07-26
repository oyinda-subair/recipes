package com.la.receta.config

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import akka.stream.Materializer
import ch.megard.akka.http.cors.scaladsl.model.{HttpHeaderRange, HttpOriginMatcher}
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.la.receta.config.http.RouteHandler

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait RouteConfig extends RouteHandler with ApplicationLogger with ApplicationConfiguration {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: Materializer = Materializer(system)
  implicit val executionContext: ExecutionContext = system.dispatcher

  private val setting = CorsSettings.defaultSettings
    .withAllowedOrigins(HttpOriginMatcher.*)
    .withAllowCredentials(false)
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
        "X-Requested-With"
      )
    )

  val routes: Route

  val handleErrors = handleRejections(rejectionHandler) & handleExceptions(myExceptionHandler)

  lazy val allRoutes = handleErrors {
    cors(setting) {
      handleErrors {
        routes
      }
    }
  }

  def runService(): Unit = {
    val binding = Http().bindAndHandle(allRoutes, httpInterface, httpPort)

    binding.onComplete {
      case Success(serverBinding) =>
        log.info(s"==> Server bound to http:/${serverBinding.localAddress}")
      case Failure(ex) =>
        log.error(s"Failed to bind to $httpInterface:$httpPort !", ex)
        system.terminate()
    }
  }
}
