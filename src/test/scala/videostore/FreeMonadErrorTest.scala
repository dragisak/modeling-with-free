package videostore

import cats.Eq
import cats.arrow.FunctionK
import cats.free.Free
import cats.laws.discipline.MonadErrorTests
import cats.tests.CatsSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import videostore.freeimpl.FreeMonadError.freeMonadError

class FreeMonadErrorTest extends CatsSuite {

  private type TestM[A] = Free[Either[String, ?], A]

  private implicit def eq[A: Eq]: Eq[TestM[A]] = Eq.by(_.foldMap(FunctionK.id))

  private implicit def arbFree[A: Arbitrary]: Arbitrary[TestM[A]] = Arbitrary(
    arbitrary[Either[Error, A]].map(Free.liftF)
  )

  checkAll("FreeMonadError", MonadErrorTests[TestM, String].monadError[Int, String, Boolean])

}
