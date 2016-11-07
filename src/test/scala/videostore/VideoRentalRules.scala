package videostore

import cats.scalatest.EitherMatchers._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks._
import videostore.VideoRental.ops._
import videostore.impl.VideoStoreInterpreter
import cats.implicits._

// scalastyle:off magic.number
abstract class VideoRentalRules(videoStoreImpl: VideoStoreInterpreter[ErrorOr]) extends WordSpec {

  import videoStoreImpl._

  private implicit val qtys = Gen.choose(1, 100).label("qty")
  private implicit val movies = implicitly[Arbitrary[Movie]].arbitrary.label("movie")

  "Video Rental App" should {

    "allow me to add DVDs to inventory" in forAll(movies, qtys) { (movie, qty) =>
      val op = addInventory(movie, qty)
      val result = interpreter().run(op)
      result shouldBe right
      val Right(dvds) = result
      dvds should have size qty.toLong
    }

    "allow me to rent a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory(movie, qty)
        dvd = dvds.head
        res <- rentDVD(dvd)
      } yield res
      val result = interpreter().run(op)
      result shouldBe right
    }

    "allow me to return a rented a DVD" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory(movie, qty)
        dvd = dvds.head
        _ <- rentDVD(dvd)
        res <- returnDVD(dvd)
      } yield res
      val result = interpreter().run(op)
      result shouldBe right
    }

    "prevent me to renting same DVD twice" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory(movie, qty)
        dvd = dvds.head
        _ <- rentDVD(dvd)
        res <- rentDVD(dvd)
      } yield res
      val result = interpreter().run(op)
      result shouldBe left
    }

    "find DVD if available" in forAll(movies, qtys) { (movie, qty) =>
      val op = for {
        dvds <- addInventory(movie, qty)
        res <- searchForDVD(movie)
      } yield (res, dvds)
      val result = interpreter().run(op)
      result shouldBe right
      val Right((searchRes, dvds)) = result
      searchRes shouldBe 'defined
      val Some(dvd) = searchRes
      dvds should contain(dvd)
    }

  }

}
