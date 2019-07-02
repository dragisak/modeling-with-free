package videostore.freeimpl

import cats.free.Free
import cats.implicits._
import cats.{Eq, MonadError}
import videostore.{Error, ErrorOr}

trait FreeTests {

  implicit def errorOrEq[A: Eq]: Eq[ErrorOr[A]] = Eq.fromUniversalEquals

  implicit def taskEq[A: Eq]: Eq[Free[Program, A]] =
    Eq.by(_.foldMap(Combined.interpreter))

  implicit def freeMonadErrorError: MonadError[FreeProgram, Error] = new MonadError[FreeProgram, Error] {

    override def flatMap[A, B](fa: FreeProgram[A])(f: A => FreeProgram[B]): FreeProgram[B] = fa.flatMap {
      case Left(err)  => raiseError(err)
      case Right(res) => f(res)
    }

    override def tailRecM[A, B](a: A)(f: A => FreeProgram[Either[A, B]]): FreeProgram[B] = f(a).flatMap {
      case Left(a)         => Free.pure(Left(a))
      case Right(Left(a))  => tailRecM(a)(f)
      case Right(Right(b)) => Free.pure(Right(b))
    }

    override def raiseError[A](e: Error): FreeProgram[A] = Free.pure(Left(e))

    override def handleErrorWith[A](fa: FreeProgram[A])(f: Error => FreeProgram[A]): FreeProgram[A] = fa.flatMap {
      case Left(err)  => f(err)
      case Right(res) => pure(res)
    }

    override def pure[A](x: A): FreeProgram[A] = Free.pure(Right(x))
  }
}
