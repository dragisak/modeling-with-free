package videostore

import cats.{Eq, MonadError}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws
import cats.laws.discipline._

trait VideoRentalTest[F[_]] extends Laws {

  def laws: VideoRentalLaws[F]

  private implicit val arbMovie: Arbitrary[Movie] = Arbitrary(Gen.alphaLowerStr)
  private implicit val arbQty: Arbitrary[Int]     = Arbitrary(Gen.choose(1, 10))

  def allTests(implicit eqInt: Eq[F[Int]], eqUnit: Eq[F[Unit]], eqBoolean: Eq[F[Boolean]]): RuleSet =
    new SimpleRuleSet(
      name = "VideoRental",
      "add inventory"                       -> forAll(laws.addInventory _),
      "rent DVD"                            -> forAll(laws.rentDvd _),
      "prevent from renting same DVD twice" -> forAll(laws.preventFromRentingSameDvdTwice _),
      "return rented DVD inventory"         -> forAll(laws.returnRentedDvd _),
      "find DVD if inventory available"     -> forAll(laws.findIfDvdIsAvailable _)
    )

}

object VideoRentalTest {
  def apply[F[_]](instance: VideoRental[F])(implicit ev: MonadError[F, Error]): VideoRentalTest[F] =
    new VideoRentalTest[F] {
      override val laws: VideoRentalLaws[F] = VideoRentalLaws(instance)
    }
}
