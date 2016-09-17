package videostore

import cats.data.Xor
import cats.scalatest.XorMatchers._
import cats.~>
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks._
import videostore.VideoRental._

// scalastyle:off magic.number
abstract class VideoRentalRules[Movie: Arbitrary, DVD](interpreter: VideoRental ~> ErrorOr) extends WordSpec {

  private implicit val qtys = Gen.choose(1, 100).label("qty")
  private implicit val movies = implicitly[Arbitrary[Movie]].arbitrary.label("movie")

  "Video Rental App" should {

    "allow me to add DVDs to inventory" in forAll(movies, qtys) { (movie, qty) =>
      val op = addInventory[Movie, DVD](movie, qty)
      val result = op.foldMap(interpreter)
      result shouldBe right
      val Xor.Right(dvds) = result
      dvds should have size qty.toLong
    }

    "allow me to rent a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory[Movie, DVD](movie, qty)
        dvd = dvds.head
        res <- rentDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe right
    }

    "allow me to return a rented a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory[Movie, DVD](movie, qty)
        dvd = dvds.head
        _ <- rentDVD(dvd)
        res <- returnDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe right
    }

    "prevent me to renting same DVD twice" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory[Movie, DVD](movie, qty)
        dvd = dvds.head
        _ <- rentDVD(dvd)
        res <- rentDVD(dvd)
      } yield res
      val result = op.foldMap(interpreter)
      result shouldBe left
    }

    "find DVD if available" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory[Movie, DVD](movie, qty)
        res <- searchForDVD[Movie, DVD](movie)
      } yield (res, dvds)

      val result = op.foldMap(interpreter)
      result shouldBe right
      val Xor.Right((searchRes, dvds)) = result
      searchRes shouldBe 'definde

      val Some(dvd) = searchRes

      dvds should contain (dvd)

    }

  }

}
