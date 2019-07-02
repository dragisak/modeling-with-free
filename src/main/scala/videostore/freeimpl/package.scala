package videostore

import cats.data.EitherK
import cats.free.Free

package object freeimpl {
  type Program[A]          = EitherK[LoggingFree.DSL, VideoRentalFree.DSL, A]
  type FreeResult[F[_], A] = Free[F, ErrorOr[A]]
  type FreeProgram[A]      = FreeResult[Program, A]
}
