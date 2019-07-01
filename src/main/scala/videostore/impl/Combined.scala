package videostore.impl

import cats.{Id, ~>}
import videostore.{ErrorOr, Program}

object Combined {

  private val idToErrorOr: Id ~> ErrorOr = new (Id ~> ErrorOr) {
    override def apply[A](fa: Id[A]): ErrorOr[A] = Right(fa)
  }

  val interpreter: Program ~> ErrorOr = (StdoutLogging.interpreter andThen idToErrorOr) or InMemory.interpreter

}
