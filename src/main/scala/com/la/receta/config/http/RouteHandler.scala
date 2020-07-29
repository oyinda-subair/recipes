package com.la.receta.config.http

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import com.la.receta.config.{ErrorResponse, Logger}
import com.la.receta.config.errorhandler._

trait RouteHandler extends Logger {

  // ----- ----- Start of Cors config ----- -----

  private lazy val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With")
  )

  //this directive adds access control headers to normal responses
  private def addAccessControlHeaders: Directive0 =
    respondWithHeaders(corsResponseHeaders)

  //this handles preflight OPTIONS requests.
  private def preflightRequestHandler: Route =
    options {
      complete(
        HttpResponse(StatusCodes.OK)
          .withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE))
      )
    }

  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route =
    addAccessControlHeaders {
      preflightRequestHandler ~ r
    }

  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCORSHeaders(response: HttpResponse): HttpResponse =
    response.withHeaders(corsResponseHeaders)

  // ----- ----- End of Cors config ----- -----

  def rejectionHandler: RejectionHandler =
    RejectionHandler
      .newBuilder()
      .handle {
        case MissingQueryParamRejection(param) =>
          val errorResponse = ErrorResponse(
            BadRequest.intValue,
            "Missing Parameter",
            s"The required $param was not found."
          ).toStrEntity
          complete(
            HttpResponse(
              BadRequest,
              entity = HttpEntity(ContentTypes.`application/json`, errorResponse)
            )
          )
      }
      .handle {
        case AuthorizationFailedRejection =>
          val errorResponse = ErrorResponse(
            Unauthorized.intValue,
            "Authorization",
            "The authorization check failed for you. Access Denied."
          ).toStrEntity
          complete(
            HttpResponse(
              Unauthorized,
              entity = HttpEntity(ContentTypes.`application/json`, errorResponse)
            )
          )
      }
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        val errorResponse = ErrorResponse(
          MethodNotAllowed.intValue,
          "Not Allowed",
          s"Access to $names is not allowed."
        ).toStrEntity
        complete(
          HttpResponse(
            MethodNotAllowed,
            entity = HttpEntity(ContentTypes.`application/json`, errorResponse)
          )
        )
      }
      .handleNotFound {
        val errorResponse = ErrorResponse(
          NotFound.intValue,
          "NotFound",
          "The requested resource could not be found."
        ).toStrEntity
        complete(
          HttpResponse(
            NotFound,
            entity = HttpEntity(ContentTypes.`application/json`, errorResponse)
          )
        )
      }
      .result()

  def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: ResourceNotFoundException => logException(NotFound, e.message, "NotFound Exception")
      case e: UnauthorizedUserException =>
        logException(Unauthorized, e.message, "Unauthorized Exception")
      case e: TokenExpiredException =>
        logException(Unauthorized, e.message, "TokenExpired Exception")
      case e: Exception =>
        extractUri { uri =>
          val errorResponse = ErrorResponse(
            InternalServerError.intValue,
            "Internal Server Error",
            e.getLocalizedMessage
          ).toStrEntity
          log.error(s"Request to $uri could not be handled normally")
          e.printStackTrace()
          complete(HttpResponse(InternalServerError, entity = errorResponse))
        }
    }

  private def logException(code: StatusCode, message: String, errorType: String): StandardRoute = {
    val errorResponse = ErrorResponse(code.intValue(), errorType, message)
    val response = HttpResponse(code, entity = errorResponse.toStrEntity)
    log.error(s"Error processing user request: $message")
    complete(response)
  }
}
