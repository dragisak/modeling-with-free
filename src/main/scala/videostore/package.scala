import java.util.UUID

import cats.data.EitherT
import cats.{Id, ~>}
import freek._
import videostore.impl.{InMemory, StdoutLogging}

import scala.concurrent.Future

package object videostore {

  type Movie = String

  type DVD = UUID

  type Error = String

  type ErrorOr[A] = Either[Error, A]

  type AsyncErrorOr[A] = EitherT[Future, Error, A]

  type PRG = Logging.DSL :|: VideoRental.DSL :|: NilDSL

  val PRG = DSL.Make[PRG]


  val idToErrorOr: Id ~> ErrorOr = new (Id ~> ErrorOr) {
    override def apply[A](fa: Id[A]): ErrorOr[A] = Right(fa)
  }

  def interpreter(): Interpreter[PRG.Cop, ErrorOr] = StdoutLogging.interpreter().interpreter.andThen(idToErrorOr) :&: InMemory.interpreter().interpreter


}
