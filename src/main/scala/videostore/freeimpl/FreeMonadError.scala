package videostore.freeimpl

import cats.arrow.FunctionK
import cats.{MonadError, ~>}
import cats.free.Free
import cats.implicits._

class FreeMonadError[F[_], E](implicit ME: MonadError[F, E]) extends MonadError[Free[F, ?], E] {
  override def raiseError[A](e: E): Free[F, A] = Free.liftF(ME.raiseError(e))

  override def handleErrorWith[A](fa: Free[F, A])(f: E => Free[F, A]): Free[F, A] =
    Free.liftF(
      fa.foldMap(FunctionK.id).handleErrorWith(e => f(e).foldMap(FunctionK.id))
    )

  override def pure[A](x: A): Free[F, A] = Free.pure(x)

  override def flatMap[A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] = fa.flatMap(f)

  override def tailRecM[A, B](a: A)(f: A => Free[F, Either[A, B]]): Free[F, B] = a.tailRecM(f)
}

object FreeMonadError {

  implicit def freeMonadError[F[_], E](implicit ME: MonadError[F, E]): MonadError[Free[F, ?], E] =
    new FreeMonadError[F, E]

}
