import java.sql.Date

import models.{Conference, ConferenceFeedback, SessionFeedback}
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

//    "send 404 on a bad request" in new WithApplication{
//      route(FakeRequest(GET, "/boum")) must beNone
//    }

//    "render the index page" in new WithApplication{
//      val home = route(FakeRequest(GET, "/")).get
//
//      status(home) must equalTo(OK)
//      contentType(home) must beSome.which(_ == "text/html")
//      contentAsString(home) must contain ("Your new application is ready.")
//    }


    "create and delete conference" in new WithApplication() {
      Conference.create(Conference(12345,"DEVIEW",2100,new Date(2015,9,15)))
      Conference.create(Conference(54321,"D2 세미나",200,new Date(2015,9,16)))

      Conference.findAll.size must_== 2
      Conference.findAll.filter(_.id == 12345).size must_== 1
      Conference.findAll.filter(_.id == 54321).size must_== 1

      Conference.findById(12345)(0).name mustEqual("DEVIEW")
      Conference.findById(54321)(0).name mustEqual("D2 세미나")

      Conference.delete(12345)

      Conference.findById(12345).size must_== 0

    }


  }
}
