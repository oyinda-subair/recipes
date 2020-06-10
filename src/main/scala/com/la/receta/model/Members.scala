package com.la.receta.model

import java.util.UUID

import com.la.receta.database.Connector._
import com.la.receta.entities.{CreateMemberRequest, Member}
import org.joda.time.DateTime
import slick.jdbc.JdbcProfile
import slick.jdbc.GetResult
import session.profile.api._
import com.github.tototoshi.slick.MySQLJodaSupport._
import com.la.receta.config.ApplicationConfiguration

import scala.concurrent.{ExecutionContext, Future}

class MemberTable(tag: Tag) extends Table[Member](tag, "user_by_id") {
  def userId = column[String]("user_id", O.PrimaryKey)
  def username = column[String]("username")
  def email = column[String]("email")
  def password = column[String]("password")
  def timestampCreated: Rep[DateTime] = column[DateTime]("timestamp_created")
  def timestampUpdated: Rep[Option[DateTime]] = column[Option[DateTime]]("timestamp_updated")

  def * =
    (
      userId,
      username,
      email,
      password,
      timestampCreated,
      timestampUpdated
    ) <> (Member.tupled, Member.unapply)
}

trait Members {
  def insert(entity: CreateMemberRequest): Future[String]
  def findById(id: String): Future[Option[Member]]
}

class MembersImpl(implicit val db: JdbcProfile#Backend#Database, ec: ExecutionContext)
    extends Members
    with ApplicationConfiguration {
  val member = TableQuery[MemberTable]

  def insert(entity: CreateMemberRequest): Future[String] = {
    val now = DateTime.now
    val userId = UUID.randomUUID().toString
    val user = Member(userId, entity.username, entity.email, entity.password, now, None)

    log.info("INSERTING into database: adding new user")
    db.run(member.insertOrUpdate(user)).map(_ => userId)
  }

  def findById(id: String): Future[Option[Member]] = {
    log.info("QUERYING the database: get user by id")
    db.run(member.filter(_.userId === id).result.headOption)
  }
}
