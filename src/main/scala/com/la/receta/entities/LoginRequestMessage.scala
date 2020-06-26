package com.la.receta.entities

import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.{Format, Json}

case class LoginRequestMessage(email: String, password: String)

object LoginRequestMessage {

  def verifyPassword(password: String, hashPassword: String): Boolean =
    BCrypt.checkpw(password, hashPassword)

  implicit val format: Format[LoginRequestMessage] = Json.format
}
