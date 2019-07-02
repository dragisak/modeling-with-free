package videostore

import cats.data.EitherK

package object freeimpl {

  type Program[A] = EitherK[LoggingFree.DSL, VideoRentalFree.DSL, A]

}
