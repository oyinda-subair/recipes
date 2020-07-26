package com.la.receta.database

import com.la.receta.model.MembersImpl
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class RecetaDao(db: JdbcProfile#Backend#Database)(implicit val ec: ExecutionContext) {

  object UserDatabase extends MembersImpl(db)
}
