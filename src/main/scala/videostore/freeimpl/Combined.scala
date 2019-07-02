package videostore.freeimpl

import cats.data.EitherK
import cats.{Id, ~>}
import videostore.ErrorOr

object Combined {

  type Program[A] = EitherK[LoggingFree.DSL, VideoRentalFree.DSL, A]

  private val idToErrorOr: Id ~> ErrorOr = new (Id ~> ErrorOr) {
    override def apply[A](fa: Id[A]): ErrorOr[A] = Right(fa)
  }

  val interpreter: Program ~> ErrorOr =
    (StdoutLogging.interpreter andThen idToErrorOr) or InMemoryVideoRental.interpreter

}
