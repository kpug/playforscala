package models

import java.sql.Timestamp
import slick.driver.MySQLDriver.api._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

case class Board(id:Option[Int], userid:Int, title:String, content:String, filepath:String, date:Timestamp)

class Boards(tag:Tag) extends Table[Board](tag, "board"){
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title", O.SqlType("varchar(100)"))
  def content = column[String]("content")
  def filepath = column[String]("filepath", O.SqlType("varchar(50)"))
  def date = column[Timestamp]("date")

  def userid = column[Int]("user_index")
  def user = foreignKey("user", userid, TableQuery[Users])(_.id)

  def * = (id.?, userid, title, content, filepath, date) <> (Board.tupled, Board.unapply)
}

object Boards extends TableQuery(new Boards(_)) {
  def totalRecord:Int = {
    val db = Database.forConfig("default")
    val query = this.map( b => b ).length.result
    Await.result(db.run(query), Duration.Inf)
  }
}