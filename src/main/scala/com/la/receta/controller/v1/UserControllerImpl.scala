package com.la.receta.controller.v1

import akka.actor.ActorSystem
import com.la.receta.config.base.Type.UserId
import com.la.receta.config.base.Util._
import com.la.receta.config.{DataResponseWrapper, Logger}
import com.la.receta.controller.UserController
import com.la.receta.database.RecetaDao
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage, MemberResponse}
import com.la.receta.config.errorhandler.{BadRequestException, ResourceNotFoundException, UnauthorizedUserException}
import com.la.receta.config.http.UserToken
import com.la.receta.model.Member

import scala.concurrent.{ExecutionContext, Future}

class UserControllerImpl(db: RecetaDao)(implicit val system: ActorSystem, ec: ExecutionContext)
    extends Logger
    with UserController {

  def createMember(request: CreateMemberRequest): Future[DataResponseWrapper[UserToken]] = {
    val password = hashString(request.password)
    val entity = Member.create(request.name, request.username, request.email, password)
    for {
      userId <- db.UserDatabase.insert(entity)
    } yield DataResponseWrapper(UserToken.generate(userId))
  }

  def login(request: LoginRequestMessage): Future[DataResponseWrapper[UserToken]] = {
    if (request.email.trim.isEmpty || request.password.trim.isEmpty) {
      log.error("Login fields are empty")
      throw BadRequestException("Login fields can't be empty")
    }

      for {
        entity <- fetchMemberByEmail(request.email)
        password = LoginRequestMessage.verifyPassword(request.password, entity.password)
      } yield
        if (password) {
          val token = UserToken.generate(entity.userId)
          DataResponseWrapper(token)
        } else {
          log.error(s"UnAuthorized User: incorrect password")
          throw UnauthorizedUserException("incorrect password")
        }
  }

  def getUserById(userId: UserId): Future[DataResponseWrapper[MemberResponse]] =
    db.UserDatabase.findById(userId).map {
      case Some(value) => DataResponseWrapper(MemberResponse(value.userId, value.name, value.username, None))
      case None        => log.error(s"ResourceNotFound: userId: ${md5HashString(userId)} was not found")
      throw ResourceNotFoundException(s"ResourceNotFound: userId: ${md5HashString(userId)} was not found")
    }

  private def fetchMemberByEmail(email: String): Future[Member] =
    db.UserDatabase.findByEmail(email).map {
      case Some(member) => member
      case None =>
        log.error(s"NotFound Error: email: $email does not exit")
        throw ResourceNotFoundException("Oops! sorry, email does not exist")
    }
}
