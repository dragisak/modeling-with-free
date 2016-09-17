import cats.data.{Xor, XorT}

import scala.concurrent.Future

package object videostore {
  type Error = String

  type ErrorOr[A] = Xor[Error, A]

  type AsyncErrorOr[A] = XorT[Future, Error, A]
}
