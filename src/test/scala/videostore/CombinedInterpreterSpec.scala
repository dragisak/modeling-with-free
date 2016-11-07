package videostore

import cats.scalatest.EitherMatchers._
import cats.implicits._
import freek._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks._

// scalastyle:off magic.number
class CombinedInterpreterSpec extends WordSpec {

  private implicit val qtys = Gen.choose(1, 100).label("qty")
  private implicit val movies = implicitly[Arbitrary[Movie]].arbitrary.label("movie")

  val VS = videostore.VideoRental.DSL
  val LOG = videostore.Logging.DSL

  "combined interpreter" should {
    "allow me to return a rented a DVD" in forAll(movies, qtys) { (movie, qty) =>

      val op = for {
        _ <- LOG.Info(s"Adding $movie").freek[PRG]
        dvds <- VS.AddInventory(movie, qty).freek[PRG]
        dvd = dvds.head
        _ <- LOG.Info(s"Renting $dvd").freek[PRG]
        _ <- VS.RentDVD(dvd).freek[PRG]
        _ <- LOG.Info(s"Returning $dvd").freek[PRG]
        res <- VS.ReturnDVD(dvd).freek[PRG]
      } yield res
      val result = op.interpret(interpreter())
      result shouldBe right
    }
  }


}
