package controllers

import models.Users
import play.api._
import play.api.libs.Crypto
import play.api.mvc._
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global


object UserController extends Controller {
  def login = Action { implicit request =>
    val user = for {
      u <- request.session.get("userid")
      p <- request.session.get("password")
    } yield Some(u + p)

    user match {
      case Some(uandp) =>
        Redirect(routes.BoardController.list(1))
      case None =>
        Ok(views.html.login())
    }
  }

  def loginProcess = Action.async (parse.urlFormEncoded) { implicit request =>
    val db = Database.forConfig("default")

    val userid = request.body("userid")(0)
    val password = Crypto.sign(request.body("password")(0))
    val query = Users.filter(u => u.userid === userid && u.password === password).result.head

    db.run(query).map {
      user =>
        Redirect(routes.BoardController.list(1)).withSession(
          "uidx" -> user.id.get.toString,
          "name" -> user.name,
          "userid" -> user.userid,
          "password" -> user.password)
    }.recover {
      case e:Exception => Redirect(routes.UserController.login)
    }
  }
}
