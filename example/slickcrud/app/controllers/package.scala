import slick.driver.MySQLDriver.api._

package object controllers {
  val db = Database.forConfig("default")
}
