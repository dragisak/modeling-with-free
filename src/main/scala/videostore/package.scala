import java.util.UUID

import cats.data.{Xor, XorT}
import freek._

import scala.concurrent.Future

package object videostore {

  type Movie = String

  type DVD = UUID

  type Error = String

  type ErrorOr[A] = Xor[Error, A]

  type AsyncErrorOr[A] = XorT[Future, Error, A]

  type PRG = Logging.DSL :|: VideoRental.DSL :|: NilDSL

  val PRG = DSL.Make[PRG]

}
