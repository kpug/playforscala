package models

import java.util.Date

import anorm._
import anorm.SqlParser._

import play.api.Play.current
import play.api.db.DB

/**
 * Created by Hanyong Jo on 15. 3. 7..
 */
case class Conference (id:Long, name:String, attendees:Long, date:Date)

object Conference {

  /**
   * Using Parser Combinator
   */
  val simple : RowParser[Conference]= {
      get[Long]("conference.id") ~
      get[String]("conference.name") ~
      get[Long]("conference.attendees") ~
      get[Date]("conference.date") map {
      case id ~ name ~ attendees ~ date => Conference(id, name, attendees, date)
    }
  }

  def findAll :List[Conference] = {
    DB.withConnection { implicit connection =>
      SQL("select * from conference").as(Conference.simple *)
    }
  }

  def findById(id : Long) : List[Conference] = {
    DB.withConnection { implicit connection =>
      SQL("select * from conference where id = {id}").on('id -> id).as(Conference.simple *)
    }
  }

  def create(conference: Conference) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into conference values (
            {id},{name}, {attendees}, {date}
          )
        """
      ).on(
          'id -> conference.id,
          'name -> conference.name,
          'attendees -> conference.attendees,
          'date -> conference.date
        ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from conference where id = {id}").on('id -> id).executeUpdate()
    }
  }


}
