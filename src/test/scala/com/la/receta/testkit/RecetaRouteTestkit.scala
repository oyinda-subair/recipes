package com.la.receta.testkit

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import play.api.libs.json.{Json, Reads, Writes}

import scala.util.Random

trait RecetaRouteTestkit {

  def toEntity[T: Reads: Writes](body: T): HttpEntity.Strict = {
    val message = Json.toJson(body).toString()
    HttpEntity(MediaTypes.`application/json`, message)
  }

  private val random = new Random(System.currentTimeMillis)
  def string10 = new String(Array.fill(10)((random.nextInt(26) + 65).toByte))
}
