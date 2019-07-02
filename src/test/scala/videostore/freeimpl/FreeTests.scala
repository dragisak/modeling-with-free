package videostore.freeimpl

import cats.arrow.FunctionK
import cats.free._
import cats.implicits._
import cats.{Eq, InjectK, MonadError}
import videostore.freeimpl.VideoRentalFree.DSL
import videostore.{Error, ErrorOr}

trait FreeTests {

  def interpreter: VideoRentalFree.Interpreter[ErrorOr]

  val inject: InjectK[DSL, ErrorOr] = new InjectK[DSL, ErrorOr] {
    override def inj: FunctionK[DSL, ErrorOr] = interpreter

    override def prj: FunctionK[ErrorOr, Lambda[A => Option[DSL[A]]]] =
      λ[FunctionK[ErrorOr, Lambda[A => Option[DSL[A]]]]] {
        case Left(err) => None
        case Right(a)  => None
      }
  }

  implicit def freeDSLEq[A: Eq]: Eq[Free[DSL, A]] =
    Eq.by(_.foldMap(interpreter))

  implicit def freeErrorOrEq[A: Eq]: Eq[Free[ErrorOr, A]] =
    Eq.by(_.foldMap(FunctionK.id))

  implicit def freeMonadErrorError[F[_]](implicit M: MonadError[F, Error]): MonadError[Free[F, ?], Error] =
    new MonadError[Free[F, ?], Error] {
      override def raiseError[A](e: Error): Free[F, A] = Free.liftF(M.raiseError(e))

      override def handleErrorWith[A](fa: Free[F, A])(f: Error => Free[F, A]): Free[F, A] = ???

      override def pure[A](x: A): Free[F, A] = Free.pure(x)

      override def flatMap[A, B](fa: Free[F, A])(f: A => Free[F, B]): Free[F, B] = fa.flatMap(f)

      override def tailRecM[A, B](a: A)(f: A => Free[F, Either[A, B]]): Free[F, B] = f(a).flatMap {
        case Left(a)  => tailRecM(a)(f)
        case Right(b) => pure(b)
      }
    }
}
