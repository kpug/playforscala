package controllers

import models.UserInfo
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def createForm = Action {
    Ok(views.html.create(userInfoForm))
  }


  val userInfoForm = Form(mapping(
    "name" -> text,               //text 앞에 Forms. 가 생략되어 있음. (상단에서 import)
    "email" -> email,             //마찬가지..
    "address" -> optional(text))(UserInfo.apply)(UserInfo.unapply)
  )

  def createUserInfo = Action { implicit request =>
    userInfoForm.bindFromRequest.fold(            //bindFromRequest()(request) 인데 생략된거임.(implicit 때문에 가능)
    formWithErrors => Ok(views.html.create(formWithErrors)),
    userInfo => Ok(userInfo.name +"\n" + userInfo.email + "\n" + userInfo.address.getOrElse("없음")))
//    userInfo => Ok("추가 완료\n" + userInfo))
  }

}
