
import play.api.libs.Crypto
import play.api.{Application, GlobalSettings}
import slick.driver.MySQLDriver.api._
import models._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

object Bootstrap extends GlobalSettings {
  override def onStart(app:Application) = {

  }

  def createSchema = {
    val db = Database.forConfig("default")
    val schemas = TableQuery[Users].schema ++ TableQuery[Boards].schema

    val setup = DBIO.seq(
      schemas.create
    )

    // Duration.Inf means Duration.Infinity
    Await.result(db.run(setup), Duration.Inf)

    val query = Users.map( u => (u.userid, u.password, u.name) ).insertOrUpdate(("test", Crypto.sign("1234"), "test user"))
    Await.result(db.run(query), Duration.Inf)
  }
}