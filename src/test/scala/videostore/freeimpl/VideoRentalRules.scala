package videostore.freeimpl

import cats.implicits._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks._
import videostore.Movie

class VideoRentalRules extends WordSpec {

  private val interpreter = Combined.interpreter
  private val videoRental = VideoRentalFree[Program]

  private implicit val qtys: Gen[Int]     = Gen.choose(1, 100).label("qty")
  private implicit val movies: Gen[Movie] = implicitly[Arbitrary[Movie]].arbitrary.label("movie")

  "Video Rental App" should {

    "allow me to add DVDs to inventory" in forAll(movies, qtys) { (movie, qty) =>
      val op     = videoRental.addInventory(movie, qty)
      val result = op.foldMap(interpreter)
      result shouldBe 'right
      val Right(dvds) = result
      dvds should have size qty.toLong
    }

    "allow me to rent a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- videoRental.addInventory(movie, qty)
        dvd  = dvds.head
        res  <- videoRental.rentDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe 'right
    }

    "allow me to return a rented a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- videoRental.addInventory(movie, qty)
        dvd  = dvds.head
        _    <- videoRental.rentDVD(dvd)
        res  <- videoRental.returnDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe 'right
    }

    "prevent me to renting same DVD twice" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- videoRental.addInventory(movie, qty)
        dvd  = dvds.head
        _    <- videoRental.rentDVD(dvd)
        res  <- videoRental.rentDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe 'left
    }

    "find DVD if available" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- videoRental.addInventory(movie, qty)
        res  <- videoRental.searchForDVD(movie)
      } yield (res, dvds)
      val result = op.foldMap(interpreter)
      result shouldBe 'right
      val Right((searchRes, dvds)) = result
      searchRes shouldBe 'defined
    }

  }

}
