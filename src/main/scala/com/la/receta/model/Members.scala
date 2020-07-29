package com.la.receta.model

import com.la.receta.database.Connector._
import com.la.receta.entities.Member
import org.joda.time.DateTime
import slick.jdbc.JdbcProfile
import session.profile.api._
import com.github.tototoshi.slick.MySQLJodaSupport._
import com.la.receta.config.Logger
import com.la.receta.config.base.Type.UserId

import scala.concurrent.{ExecutionContext, Future}

class MemberTable(tag: Tag) extends Table[Member](tag, "user_by_id") {
  def userId: Rep[String] = column[String]("user_id", O.PrimaryKey)
  def name: Rep[String] = column[String]("full_name", O.Unique)
  def username: Rep[String] = column[String]("username", O.Unique)
  val indexUsername = index("index_username", username, unique = true)
  def email: Rep[String] = column[String]("email", O.Unique)
  val indexEmail = index("index_email", email, unique = true)
  def password: Rep[String] = column[String]("password")
  def timestampCreated: Rep[DateTime] = column[DateTime]("timestamp_created")
  def timestampUpdated: Rep[Option[DateTime]] = column[Option[DateTime]]("timestamp_updated")

  def * =
    (
      userId,
      name,
      username,
      email,
      password,
      timestampCreated,
      timestampUpdated
    ) <> ((Member.apply _).tupled, Member.unapply)
}

trait Members {
  def insert(entity: Member): Future[String]
  def findById(id: String): Future[Option[Member]]
  def findByEmail(email: String): Future[Option[Member]]
}

class MembersImpl(db: JdbcProfile#Backend#Database)(implicit val ec: ExecutionContext)
    extends Members
    with Logger {
  val member = TableQuery[MemberTable]

  def insert(entity: Member): Future[String] = {
    log.info("INSERTING into database: adding new user")
    db.run(member.insertOrUpdate(entity)).map(_ => entity.userId)
  }

  def findById(id: UserId): Future[Option[Member]] = {
    log.info("QUERYING the database: get user by id")
    db.run(member.filter(_.userId === id).result.headOption)
  }

  def findByEmail(email: String): Future[Option[Member]] = {
    log.info("QUERYING the database: get user by email")
    db.run(member.filter(_.email === email).result.headOption)
  }
}
