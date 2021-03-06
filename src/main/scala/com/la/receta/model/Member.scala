package com.la.receta.model

import java.util.UUID

import com.la.receta.config.FormatEntity
import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

case class Member(
  userId: String,
  name: String,
  username: String,
  email: String,
  password: String,
  timestampCreated: DateTime,
  timestampUpdated: Option[DateTime]
)

object Member extends FormatEntity[Member] {

  def create(name: String, username: String, email: String, password: String): Member = {
    val now = DateTime.now
    val userId = UUID.randomUUID().toString
    Member(userId, name, username, email, password, now, None)
  }

  implicit val format: Format[Member] = Json.format
}
