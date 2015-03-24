package models

import java.sql.Timestamp
import java.util.Date
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id:Option[Int], userid:String, password:String, name:String, date:Timestamp)
class Users(tag:Tag) extends Table[User](tag, "user") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def userid = column[String]("userid", O.SqlType("varchar(30)"))
  def password = column[String]("password", O.SqlType("varchar(50)"))
  def name = column[String]("name", O.SqlType("varchar(30)"))
  def date = column[Timestamp]("date")

  def * = (id.?, userid, password, name, date) <>(User.tupled, User.unapply)
}

object Users extends TableQuery(new Users(_)) {
  def findById(uidx:Int): User = {
    val db = Database.forConfig("default")
    val query = this.filter(_.id === uidx).result.head
    Await.result(db.run(query), Duration.Inf)
  }
}