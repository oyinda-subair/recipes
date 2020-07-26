package com.la.receta.config

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class DataResponseWrapper[+T](data: T)

object DataResponseWrapper {

  implicit def fmt[T](implicit fmt: Format[T]): Format[DataResponseWrapper[T]] =
    new Format[DataResponseWrapper[T]] {

      override def writes(o: DataResponseWrapper[T]): JsValue =
        JsObject(Seq("data" -> Json.toJson(o.data)))

      override def reads(json: JsValue): JsResult[DataResponseWrapper[T]] =
        JsSuccess(
          DataResponseWrapper(
            (json \ "data").as[T]
          )
        )
    }

}
