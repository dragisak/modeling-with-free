import java.util.UUID

import cats.data.{Xor, XorT}
import cats.{Id, ~>}
import cats.syntax.xor._
import freek._
import videostore.impl.{InMemory, StdoutLogging}

import scala.concurrent.Future

package object videostore {

  type Movie = String

  type DVD = UUID

  type Error = String

  type ErrorOr[A] = Xor[Error, A]

  type AsyncErrorOr[A] = XorT[Future, Error, A]

  type PRG = Logging.DSL :|: VideoRental.DSL :|: NilDSL

  val PRG = DSL.Make[PRG]


  val idToErrorOr: Id ~> ErrorOr = new (Id ~> ErrorOr) {
    override def apply[A](fa: Id[A]): ErrorOr[A] = fa.right
  }

  def interpreter(): Interpreter[PRG.Cop, ErrorOr] = StdoutLogging.interpreter().interpreter.andThen(idToErrorOr) :&: InMemory.interpreter().interpreter


}
