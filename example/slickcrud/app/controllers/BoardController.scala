package controllers

import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat

import models.{Users, Boards}
import play.api._
import play.api.mvc._
import play.api.data.Forms._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class BoardVO(id:Int, name:String, title:String, content:String, date:Timestamp, filepath:String)

object BoardController extends Controller {
  def list(page:Int) = Action { implicit request =>

    val totalRecord = Boards.totalRecord
    val start = (page-1) * 10
    val query = (Boards joinLeft Users on (_.userid === _.id))
                  .sortBy { case (b, u) => b.id.desc }
                  .drop(start).take(10)
                  .map { case (b, u) => (b.id, u.map(_.name).get, b.title, b.content, b.date, b.filepath) }
                  .result

    val startPage = 1
    val vector = Await.result(db.run(query), Duration.Inf)

    Ok(views.html.list(vector.toList.map( x => BoardVO.tupled(x) ), startPage, page))
  }

  def view(id:Int) = Action { implicit request =>
    val query = (Boards joinLeft Users on (_.userid === _.id))
                  .filter { case (b, u) => b.id === id }
                  .map { case (b, u) => (b.id, u.map(_.name).get, b.title, b.content, b.date, b.filepath) }
                  .result.head

    val board = BoardVO.tupled(Await.result(db.run(query), Duration.Inf))

    Ok(views.html.view(board))
  }

  def edit(id:Int) = Action { implicit request =>
    val query = (Boards joinLeft Users on (_.userid === _.id))
                  .filter { case (b, u) => b.id === id }
                  .map { case (b, u) => (b.id, u.map(_.name).get, b.title, b.content, b.date, b.filepath) }
                  .result.head

    val board = BoardVO.tupled(Await.result(db.run(query), Duration.Inf))
    val user = Users.findById(request.session("uidx").toInt)

    Ok(views.html.edit(Some(board), user))
  }

  def delete(id:Int) = Action {
    val query = Boards.filter(_.id === id).delete
    Await.result(db.run(query), Duration.Inf)
    Redirect(routes.BoardController.list(1))
  }

  def write = Action { implicit request =>
    val user = Users.findById(request.session("uidx").toInt)
    Ok(views.html.edit(None, user))
  }

  def post = Action(parse.multipartFormData){ implicit request =>
    val form = request.body.asFormUrlEncoded
    val fileOpt = request.body.file("file")
    val filepath = for {
        file <- fileOpt
      } yield {
        val filename = file.filename
        val ext = filename.split('.').last
        val filepath = "/uploaded/" + System.currentTimeMillis + "." + ext
        file.ref.moveTo(new File("public" + filepath))

        filepath
      }

    val boardId = form("id").headOption.map(_.toInt).getOrElse(0)
    val isModification = form("id").headOption.map(_ != 0).getOrElse(false)

    val query = if(isModification) {
      Boards.filter(_.id === boardId)
        .map(b => (b.title, b.content, b.filepath))
        .update(form("title")(0), form("content")(0), filepath.getOrElse(""))
    } else {
      Boards.map(b => (b.userid, b.title, b.content, b.filepath))
        .forceInsert((request.session("uidx").toInt, form("title")(0), form("content")(0), filepath.getOrElse("")))
    }

    Await.result(db.run(query), Duration.Inf)

    if(isModification)
      Redirect(routes.BoardController.view(boardId))
    else
      Redirect(routes.BoardController.list(1))
  }
}