import java.util.UUID

import cats.data.{EitherK, EitherT}

import scala.concurrent.Future

package object videostore {

  type Movie = String

  type DVD = UUID

  type Error = String

  type ErrorOr[A] = Either[Error, A]

  type AsyncErrorOr[A] = EitherT[Future, Error, A]

  type Program[A] = EitherK[Logging.DSL, VideoRental.DSL, A]
}
