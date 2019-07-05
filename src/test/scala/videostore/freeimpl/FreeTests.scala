package videostore.freeimpl

import cats.arrow.FunctionK
import cats.free._
import cats.implicits._
import cats.{Eq, InjectK, ~>}
import videostore.ErrorOr
import videostore.freeimpl.VideoRentalFree.DSL

trait FreeTests {

  def interpreter: VideoRentalFree.Interpreter[ErrorOr]

  implicit val inject: InjectK[DSL, ErrorOr] = new InjectK[DSL, ErrorOr] {
    override def inj: DSL ~> ErrorOr = interpreter

    type Opt[A] = Option[DSL[A]]

    override def prj: ErrorOr ~> Opt = new (ErrorOr ~> Opt) {
      override def apply[A](fa: ErrorOr[A]): Option[DSL[A]] = fa match {
        case Left(err) => None // ??
        case Right(a)  => None // ??
      }
    }
  }

  implicit def freeDSLEq[A: Eq]: Eq[Free[DSL, A]] =
    Eq.by(_.foldMap(interpreter))

  implicit def freeErrorOrEq[A: Eq]: Eq[Free[ErrorOr, A]] =
    Eq.by(_.foldMap(FunctionK.id))

}
