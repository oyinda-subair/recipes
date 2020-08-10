package com.la.receta.entities

import play.api.libs.json.{Format, Json}

case class MemberResponse (
                            userId: String,
                            name: String,
                            username: String,
                            email: Option[String],
                          )

object MemberResponse {
  implicit val format: Format[MemberResponse] = Json.format
}
