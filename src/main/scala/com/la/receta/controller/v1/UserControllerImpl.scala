package com.la.receta.controller.v1

import akka.actor.ActorSystem
import com.la.receta.config.{ApplicationLogger, DataResponseWrapper}
import com.la.receta.controller.UserController
import com.la.receta.database.RecetaDao
import com.la.receta.entities.{CreateMemberRequest, LoginRequestMessage, Member}
import com.la.receta.errorhandler.{ResourceNotFoundException, UnauthorizedUserException}
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.{ExecutionContext, Future}

class UserControllerImpl(db: RecetaDao)(implicit val system: ActorSystem, ec: ExecutionContext)
    extends ApplicationLogger
    with UserController {

  def createMember(request: CreateMemberRequest): Future[DataResponseWrapper[String]] = {
    val password = BCrypt.hashpw(request.password, BCrypt.gensalt())
    val entity = Member.create(request.username, request.email, password)
    for {
      userId <- db.UserDatabase.insert(entity)
    } yield DataResponseWrapper(userId)
  }

  def login(request: LoginRequestMessage): Future[DataResponseWrapper[String]] =
    for {
      entity <- fetchMemberByEmail(request.email)
      password = LoginRequestMessage.verifyPassword(request.password, entity.password)
    } yield
      if (password)
        DataResponseWrapper(entity.userId)
      else {
        log.error(s"UnAuthorized User: email: ${request.email} does not exit")
        throw UnauthorizedUserException("incorrect email/password")
      }

  private def fetchMemberByEmail(email: String): Future[Member] =
    db.UserDatabase.findByEmail(email).map {
      case Some(member) => member
      case None =>
        log.error(s"NotFound Error: email: $email does not exit")
        throw ResourceNotFoundException("Oops! sorry, email does not exist")
    }
}
