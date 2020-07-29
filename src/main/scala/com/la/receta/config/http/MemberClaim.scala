package com.la.receta.config.http

import com.la.receta.config.FormatEntity
import play.api.libs.json.{Format, Json}

case class MemberClaim(userId: String) {
  override def toString: String = Json.toJson(this).toString
}

object MemberClaim extends FormatEntity[MemberClaim] {
  implicit val format: Format[MemberClaim] = Json.format
}
