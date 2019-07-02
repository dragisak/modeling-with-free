package videostore

import cats.data.EitherK

package object free {

  type Program[A] = EitherK[LoggingFree.DSL, VideoRentalFree.DSL, A]

}
