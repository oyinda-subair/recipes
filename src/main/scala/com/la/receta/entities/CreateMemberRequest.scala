package com.la.receta.entities

import play.api.libs.json.{Format, Json}

case class CreateMemberRequest(username: String, email: String, password: String)

object CreateMemberRequest {
  implicit val format: Format[CreateMemberRequest] = Json.format
}
