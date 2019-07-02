package videostore.free

import cats.implicits._
import cats.scalatest.EitherMatchers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._
import videostore.Movie

class CombinedInterpreterSpec extends WordSpec {

  private implicit val qtys   = Gen.choose(1, 100).label("qty")
  private implicit val movies = implicitly[Arbitrary[Movie]].arbitrary.label("movie")

  private val log         = LoggingFree[Program]
  private val videoRental = VideoRentalFree[Program]

  "combined interpreter" should {
    "allow me to return a rented a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        _    <- log.info(s"Adding $movie")
        dvds <- videoRental.addInventory(movie, qty)
        dvd  = dvds.head
        _    <- log.info(s"Renting $dvd")
        _    <- videoRental.rentDVD(dvd)
        _    <- log.info(s"Returning $dvd")
        res  <- videoRental.returnDVD(dvd)
      } yield res

      val result = op.foldMap(Combined.interpreter)
      result shouldBe right
    }
  }

}
